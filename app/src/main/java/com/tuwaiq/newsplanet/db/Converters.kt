package com.tuwaiq.newsplanet.db

import androidx.room.TypeConverter
import com.tuwaiq.newsplanet.models.Source

class Converters {

    // to tell room that this is a converter function we need to use @TypeConverter
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        // first name is the source id that given too the function ..
        return Source(name, name)
    }
}