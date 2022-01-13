package com.tuwaiq.newsplanet.db

import android.content.Context
import androidx.room.*
import com.tuwaiq.newsplanet.models.Article

@Database(
    entities = [Article::class],
    version = 1,
    // after changing the variable types in Article Dataclass I nedded to change the version and do the autoMigration ..
    //autoMigrations = [AutoMigration (from = 1, to = 2) ]
)

// to tell the database where are the converters
@TypeConverters(Converters::class)


abstract class ArticleDatabase : RoomDatabase() {

    // this function returns article Dao .. the implementation of this function will be by RoomDatabase class ..
    abstract fun getArticleDao(): ArticleDao

    // object to create Database .. Singleton ..
    companion object {
        // initially it's null .. and it's Volatile that's mean other threads can see when a thread changes this instance ..
        // this instance will be used to access the ArticleDao ..
        @Volatile
        private var instance: ArticleDatabase? = null

        // this val will synchronise setting the instance to make sure there is a single instance of this database always ..
        private val LOCK = Any()


        // this function will be called everytime we are dealing with this object ..
        // في حالة LOCK ولا ثريد يقدر يعدل عليه طالما احنا شغالين على الـinstance
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            // if it's null we call the createDatabase fun ..
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}