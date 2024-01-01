package com.rmd.business.newsapp.domain.model

data class APIResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)