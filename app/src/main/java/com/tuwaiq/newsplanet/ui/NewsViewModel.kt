package com.tuwaiq.newsplanet.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.newsplanet.NewsApplication
import com.tuwaiq.newsplanet.models.Article
import com.tuwaiq.newsplanet.models.NewsResponse
import com.tuwaiq.newsplanet.models.User
import com.tuwaiq.newsplanet.repo.NewsRepo
import com.tuwaiq.newsplanet.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

// used AndroidViewModel to use the Application context in internet connection .. and able to use getApplication() ..
class NewsViewModel(val app: Application, val newsRepo: NewsRepo) : AndroidViewModel(app) {

    val userCollectionRef = Firebase.firestore.collection("users")
    val db = FirebaseFirestore.getInstance()


    // LiveData object ..
    val topHeadlineNewsWithCategory: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()



    // I declare the page number here in the viewModel cuz if it's in the fragment it will reset with any change ..
    var topHeadlinesPageWithCategoryPage = 1

    var searchNewsPage = 1


    // this is saved here to handle the response if the activity or fragment changed .. like rotate for example ..
    var topHeadlinesGeneralResponse: NewsResponse? = null

    var searchNewsResponse: NewsResponse? = null

    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    var newCategory : String? =null
    var oldCategory : String? = null


    // General ..
    fun getTopHeadlinesWithCategory(countryCode: String, category: String, vm : LifecycleOwner) : MutableLiveData<Resource<NewsResponse>> {
        val generalLiveData = MutableLiveData<Resource<NewsResponse>>()
        viewModelScope.launch {
            safeTopHeadlinesNewsWithCategoryCall(countryCode, category).observe(vm, {
                generalLiveData.postValue(it)
            })
        }
        return generalLiveData
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    // General ..
     fun handleHeadlinesNewsWithCategoryResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if(oldCategory != newCategory){
                    topHeadlinesPageWithCategoryPage = 1
                    topHeadlinesGeneralResponse = resultResponse
                    oldCategory = newCategory
                } else if (topHeadlinesGeneralResponse == null ) {
                    topHeadlinesGeneralResponse = resultResponse
                    oldCategory = newCategory
                } else {
                    // if there is a response already .. I pass the articles from the newResponse to the oldResponse ..
                    val oldArticles = topHeadlinesGeneralResponse?.articles
                    val newArticles = resultResponse.articles

                    // i changes List<Article> to MutableList<Article> so i can add to it here ..
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topHeadlinesGeneralResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // check and set the searchNewsResponse .. first search or new search ..
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    // first increase the page number ..
                    searchNewsPage++
                    // if there is a response already .. I pass the articles from the newResponse to the oldResponse ..
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    // i changes List<Article> to MutableList<Article> so i can add to it here ..
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    // this function uses a suspend function so it needs to use coroutines ..
    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepo.upsert(article)
    }

    fun getSavedNews() = newsRepo.getSavedNews()

    // this is also a function uses a suspend function so it needs to use coroutines ..
    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepo.deleteArticle(article)
    }

    // General ..
    private suspend fun safeTopHeadlinesNewsWithCategoryCall(countryCode: String, category: String) : MutableLiveData<Resource<NewsResponse>> {
        topHeadlineNewsWithCategory.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getToHeadlinesNewsWithCategory(countryCode, category, topHeadlinesPageWithCategoryPage)
                topHeadlineNewsWithCategory.postValue(handleHeadlinesNewsWithCategoryResponse(response))
            } else {
                topHeadlineNewsWithCategory.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                // topHeadlines function could also cause an exception this why I need when here ..
                is IOException -> topHeadlineNewsWithCategory.postValue(Resource.Error("Network Failure"))
                else -> topHeadlineNewsWithCategory.postValue(Resource.Error("Another Error not IOException"))
            }
        }
        return topHeadlineNewsWithCategory
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    // this function is to check the internet connectivity .. cuz there are some things I don't want to run if there is no connection ..
    fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // here is to select wish function to use to check for connectivity cuz it's changed during apis updates ..
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun saveUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        try {
            userCollectionRef.document("$userUid").set(user).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(app.applicationContext, "Successfully saved data", Toast.LENGTH_LONG)
                    .show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(app.applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}