package com.rmd.business.newsapp.data.mapper

import androidx.room.TypeConverter
import com.rmd.business.newsapp.domain.model.Source

class ArticleSourceConverter {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(id = name, name = name)
    }
}