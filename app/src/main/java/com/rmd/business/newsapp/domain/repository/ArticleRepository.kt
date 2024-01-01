package com.rmd.business.newsapp.domain.repository

import com.rmd.business.newsapp.data.api.RetrofitInstance
import com.rmd.business.newsapp.data.datasource.local.db.ArticleDB
import com.rmd.business.newsapp.data.datasource.local.entity.ArticleEntity

class ArticleRepository(private val db: ArticleDB) {
    suspend fun getHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    suspend fun searchForNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: ArticleEntity) = db.getArticleDao().upsertArticle(article)

    suspend fun getFavoriteNew() = db.getArticleDao().getAllArticle()

    suspend fun deleteArticle(article: ArticleEntity) = db.getArticleDao().deleteArticle(article)
}

