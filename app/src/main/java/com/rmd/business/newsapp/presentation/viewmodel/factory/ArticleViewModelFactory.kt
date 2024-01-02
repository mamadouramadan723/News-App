package com.rmd.business.newsapp.presentation.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rmd.business.newsapp.domain.repository.ArticleRepository
import com.rmd.business.newsapp.presentation.viewmodel.ArticleViewModel

class ArticleViewModelFactory(private val app: Application, private val articleRepository: ArticleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticleViewModel(app, articleRepository) as T
    }
}