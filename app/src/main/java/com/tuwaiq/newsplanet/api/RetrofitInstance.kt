package com.tuwaiq.newsplanet.api

import com.tuwaiq.newsplanet.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// this class to makes me do the request from anyplace in my project ..
class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                // this will determine how the response will converted to from json response to kotlin object ..
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        // this is the api object that we will use from everywhere to make network requests ..
        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}

