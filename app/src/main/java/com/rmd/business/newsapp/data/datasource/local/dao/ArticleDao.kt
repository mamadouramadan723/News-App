package com.rmd.business.newsapp.data.datasource.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.rmd.business.newsapp.data.datasource.local.entity.ArticleEntity

@Dao
interface ArticleDao {
    @Upsert
    suspend fun upsertArticle(article: ArticleEntity): Long

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun getArticleById(id: Int): ArticleEntity?

    @Query("SELECT * FROM articles")
    suspend fun getAllArticle(): LiveData<List<ArticleEntity>>

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)
}