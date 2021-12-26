package com.tuwaiq.newsplanet.repo


import com.tuwaiq.newsplanet.api.RetrofitInstance
import com.tuwaiq.newsplanet.db.ArticleDatabase
import com.tuwaiq.newsplanet.models.Article

// inside it an object from the database to access the functions in that database ..
class NewsRepo(val db: ArticleDatabase) {


    // this function is to get the top headlines from NewsApi .. and it's suspend cuz network functions are suspend ..
    suspend fun getToHeadlinesNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getTopHeadlines(countryCode, pageNumber)

    suspend fun getToHeadlinesNewsWithCategory(countryCode: String, category : String, pageNumber: Int) =
        RetrofitInstance.api.getTopHeadlinesWithCategory(countryCode, category ,pageNumber)

    // this function is to search in all the news in the api .. and it calls for searchForNews from the api interface ..
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)


    suspend fun upsert(article: Article) = db.getArticleDao().insertArticle(article)

    // this is not suspend cuz it's return a LiveData ..
    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}