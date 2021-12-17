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

            // this is to log every responses in retrofit for debugging purposes ..
            val logging = HttpLoggingInterceptor()
            // to declare the lever of response
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                // this will determine how the response will converte to from json response to kotlin object ..
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        // this is the api object that we will use from everywhere to make network requests ..
        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}

//object FlickrBuilder {
//    private const val BASE_URL = "https://api.flickr.com/services/rest/"
//    private fun retrofit(): Retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    val flickrApi: FlickrAPI = retrofit().create(FlickrAPI::class.java)
//}

