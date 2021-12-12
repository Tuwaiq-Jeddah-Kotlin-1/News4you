package com.tuwaiq.newsplanet.repo


import com.tuwaiq.newsplanet.api.RetrofitInstance
import com.tuwaiq.newsplanet.db.ArticleDatabase

// inside it an object from the database to access the functions in that database ..
class NewsRepo(val db: ArticleDatabase) {

    // this function is to get the top headlines from NewsApi .. and it's suspend cuz network functions are suspend ..
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getTopHeadlines(countryCode, pageNumber)

}