package com.rmd.business.newsapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rmd.business.newsapp.data.datasource.local.entity.ArticleEntity
import com.rmd.business.newsapp.domain.model.APIResponse
import com.rmd.business.newsapp.domain.repository.ArticleRepository
import com.rmd.business.newsapp.presentation.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class ArticleViewModel(app: Application, private val articleRepository: ArticleRepository) :
    AndroidViewModel(app) {

    private var headlines: MutableLiveData<Resource<APIResponse>> = MutableLiveData()
    private var headlinePage = 1
    private var headlinesResponse: APIResponse? = null

    private val searchNews: MutableLiveData<Resource<APIResponse>> = MutableLiveData()
    private var searchNewsPage = 1
    private var searchNewsResponse: APIResponse? = null
    private var newSearchQuery: String? = null
    private var oldSearchQuery: String? = null

    init {
        getHeadLines("us")
    }

    private fun getHeadLines(countryCode: String) = viewModelScope.launch {
        getHeadlinesViewModel(countryCode)
    }

    private fun searchForNews(searchQuery: String) = viewModelScope.launch {
        searchNewsViewModel(searchQuery)
    }

    private fun addToFavoritesViewModel(article: ArticleEntity) = viewModelScope.launch {
        articleRepository.upsert(article)
    }

    private fun getFavoritesViewModel() = viewModelScope.launch {
        articleRepository.getFavorites()
    }

    private fun deleteArticleViewModel(article: ArticleEntity) = viewModelScope.launch {
        articleRepository.deleteArticle(article)
    }

    private suspend fun getHeadlinesViewModel(countryCode: String) {
        headlines.postValue(Resource.Loading())
        try {
            if (isConnected(this.getApplication())) {
                val response = articleRepository.getHeadlines(countryCode, headlinePage)
                headlines.postValue(handleHeadlinesResponse(response))
            } else {
                headlines.postValue((Resource.Error("No Internet Connexion")))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> headlines.postValue(Resource.Error("Unable to Connect"))
                else -> headlines.postValue(Resource.Error("No Signal"))
            }
        }
    }

    private suspend fun searchNewsViewModel(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue((Resource.Loading()))

        try {
            if (isConnected(this.getApplication())) {
                val response = articleRepository.searchForNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No Internet Connexion"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Unable to Connect"))
                else -> searchNews.postValue(Resource.Error("No Signal"))
            }
        }
    }

    private fun handleHeadlinesResponse(response: Response<APIResponse>): Resource<APIResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinePage++
                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    val oldArticles = headlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<APIResponse>): Resource<APIResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun isConnected(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport((NetworkCapabilities.TRANSPORT_WIFI)) -> true
                    hasTransport((NetworkCapabilities.TRANSPORT_CELLULAR)) -> true
                    else -> false
                }
            } ?: false
        }
    }
}