package com.rmd.business.newsapp.data.datasource.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rmd.business.newsapp.data.datasource.local.dao.ArticleDao
import com.rmd.business.newsapp.data.datasource.local.entity.ArticleEntity
import com.rmd.business.newsapp.data.mapper.ArticleSourceConverter

@Database(
    entities = [ArticleEntity::class],
    version = 1
)

@TypeConverters(ArticleSourceConverter::class)
abstract class ArticleDB : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDB? = null
        private val LOCK = Any()

        private fun createDB(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ArticleDB::class.java,
            "article_db.db"
        ).build()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDB(context).also {
                instance = it
            }
        }
    }
}