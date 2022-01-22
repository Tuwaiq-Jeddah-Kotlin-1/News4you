package com.tuwaiq.newsplanet.api

import com.tuwaiq.newsplanet.models.NewsResponse
import com.tuwaiq.newsplanet.util.Constants.Companion.API_KEY
import com.tuwaiq.newsplanet.util.Constants.Companion.API_KEY2
import com.tuwaiq.newsplanet.util.Constants.Companion.API_KEY3
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

var currentKey : String = API_KEY2

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        countryCode : String = "us",
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = currentKey
    ) : Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesWithCategory(
        @Query("country")
        countryCode : String = "us",
        @Query("category")
        category : String = "general",
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = currentKey
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery : String ,
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = currentKey
    ) : Response<NewsResponse>
}