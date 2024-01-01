package com.rmd.business.newsapp.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rmd.business.newsapp.domain.model.Source
import java.io.Serializable

@Entity(
    tableName = "articles"
)

data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
): Serializable