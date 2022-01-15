package com.tuwaiq.newsplanet.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.newsplanet.NewsApplication
import com.tuwaiq.newsplanet.api.currentKey
import com.tuwaiq.newsplanet.models.Article
import com.tuwaiq.newsplanet.models.NewsResponse
import com.tuwaiq.newsplanet.models.User
import com.tuwaiq.newsplanet.repo.NewsRepo
import com.tuwaiq.newsplanet.util.Constants.Companion.API_KEY2
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
    var user: User = User()


    // LiveData object ..
    val topHeadlineNewsTechnology: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val topHeadlineNewsSports: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val topHeadlineNewsScience: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val topHeadlineNewsBusiness: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val topHeadlineNewsHealth: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val topHeadlineNewsEntertainment: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val topHeadlineNewsGeneral: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()


    // I declare the page number here in the viewModel cuz if it's in the fragment it will reset with any change ..
    var topHeadlinesPage = 1
    var topHeadlinesPageTechnologyPage = 1
    var topHeadlinesPageSportsPage = 1
    var topHeadlinesPageSciencePage = 1
    var topHeadlinesPageBusinessPage = 1
    var topHeadlinesPageHealthPage = 1
    var topHeadlinesPageEntertainmentPage = 1
    var topHeadlinesPageGeneralPage = 1

    var searchNewsPage = 1


    // this is saved here to handle the response if the activity or fragment changed .. like rotate for example ..
    var topHeadlinesTechnologyResponse: NewsResponse? = null
    var topHeadlinesSportsResponse: NewsResponse? = null
    var topHeadlinesScienceResponse: NewsResponse? = null
    var topHeadlinesBusinessResponse: NewsResponse? = null
    var topHeadlinesHealthResponse: NewsResponse? = null
    var topHeadlinesEntertainmentResponse: NewsResponse? = null
    var topHeadlinesGeneralResponse: NewsResponse? = null

    var searchNewsResponse: NewsResponse? = null

    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null


    init {
        getTopHeadlinesGeneral("us", "general")
        getTopHeadlinesTechnology("us", "technology")
        getTopHeadlinesSports("us", "sports")
        getTopHeadlinesScience("us", "science")
        getTopHeadlinesBusiness("us", "business")
        getTopHeadlinesHealth("us", "health")
        getTopHeadlinesEntertainment("us", "entertainment")
    }


    // this is a coroutines function I used with viewModelScope that will stay alive as long as this viewModel alive ..
    // Technology ..
    fun getTopHeadlinesTechnology(countryCode: String, category: String) = viewModelScope.launch {
        safeTopHeadlinesNewsTechnologyCall(countryCode, category)
    }

    // Sports ..
    fun getTopHeadlinesSports(countryCode: String, category: String) = viewModelScope.launch {
        safeTopHeadlinesNewsSportsCall(countryCode, category)
    }

    // Science ..
    fun getTopHeadlinesScience(countryCode: String, category: String) = viewModelScope.launch {
        safeTopHeadlinesNewsScienceCall(countryCode, category)
    }

    // Business ..
    fun getTopHeadlinesBusiness(countryCode: String, category: String) = viewModelScope.launch {
        safeTopHeadlinesNewsBusinessCall(countryCode, category)
    }

    // Health ..
    fun getTopHeadlinesHealth(countryCode: String, category: String) = viewModelScope.launch {
        safeTopHeadlinesNewsHealthCall(countryCode, category)
    }

    // Entertainment ..
    fun getTopHeadlinesEntertainment(countryCode: String, category: String) =
        viewModelScope.launch {
            safeTopHeadlinesNewsEntertainmentCall(countryCode, category)
        }

    // General ..
    fun getTopHeadlinesGeneral(countryCode: String, category: String) = viewModelScope.launch {
        safeTopHeadlinesNewsGeneralCall(countryCode, category)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    // in this function I check if the response is successful or not and send the message to the Resource ..
    // Technology ..
    fun handleHeadlinesNewsTechnologyResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // first ingress the page number ..
                topHeadlinesPageTechnologyPage++
                // check and set the topHeadlinesResponse ..
                if (topHeadlinesTechnologyResponse == null) {
                    topHeadlinesTechnologyResponse = resultResponse
                } else {
                    // if there is a response already .. I pass the articles from the newResponse to the oldResponse ..
                    val oldArticles = topHeadlinesTechnologyResponse?.articles
                    val newArticles = resultResponse.articles
                    // i changes List<Article> to MutableList<Article> so i can add to it here ..
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topHeadlinesTechnologyResponse ?: resultResponse)
            }
        } else {
            currentKey = API_KEY2
            handleHeadlinesNewsTechnologyResponse(response)
        }
        return Resource.Error(response.message())
    }

    // Sports ..
    private fun handleHeadlinesNewsSportsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // first ingress the page number ..
                topHeadlinesPageSportsPage++
                // check and set the topHeadlinesResponse ..
                if (topHeadlinesSportsResponse == null) {
                    topHeadlinesSportsResponse = resultResponse
                } else {
                    // if there is a response already .. I pass the articles from the newResponse to the oldResponse ..
                    val oldArticles = topHeadlinesSportsResponse?.articles
                    val newArticles = resultResponse.articles
                    // i changes List<Article> to MutableList<Article> so i can add to it here ..
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topHeadlinesSportsResponse ?: resultResponse)
            }
        } else {
            currentKey = API_KEY2
            handleHeadlinesNewsSportsResponse(response)
        }
        return Resource.Error(response.message())
    }

    // Science ..
    private fun handleHeadlinesNewsScienceResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // first ingress the page number ..
                topHeadlinesPageSciencePage++
                // check and set the topHeadlinesResponse ..
                if (topHeadlinesScienceResponse == null) {
                    topHeadlinesScienceResponse = resultResponse
                } else {
                    // if there is a response already .. I pass the articles from the newResponse to the oldResponse ..
                    val oldArticles = topHeadlinesScienceResponse?.articles
                    val newArticles = resultResponse.articles
                    // i changes List<Article> to MutableList<Article> so i can add to it here ..
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topHeadlinesScienceResponse ?: resultResponse)
            }
        } else {
            currentKey = API_KEY2
            handleHeadlinesNewsScienceResponse(response)
        }
        return Resource.Error(response.message())
    }

    // Business ..
    private fun handleHeadlinesNewsBusinessResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // first ingress the page number ..
                topHeadlinesPageBusinessPage++
                // check and set the topHeadlinesResponse ..
                if (topHeadlinesBusinessResponse == null) {
                    topHeadlinesBusinessResponse = resultResponse
                } else {
                    // if there is a response already .. I pass the articles from the newResponse to the oldResponse ..
                    val oldArticles = topHeadlinesBusinessResponse?.articles
                    val newArticles = resultResponse.articles
                    // i changes List<Article> to MutableList<Article> so i can add to it here ..
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topHeadlinesBusinessResponse ?: resultResponse)
            }
        } else {
            currentKey = API_KEY2
            handleHeadlinesNewsBusinessResponse(response)
        }
        return Resource.Error(response.message())
    }

    // Health ..
    private fun handleHeadlinesNewsHealthResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // first ingress the page number ..
                topHeadlinesPageHealthPage++
                // check and set the topHeadlinesResponse ..
                if (topHeadlinesHealthResponse == null) {
                    topHeadlinesHealthResponse = resultResponse
                } else {
                    // if there is a response already .. I pass the articles from the newResponse to the oldResponse ..
                    val oldArticles = topHeadlinesHealthResponse?.articles
                    val newArticles = resultResponse.articles
                    // i changes List<Article> to MutableList<Article> so i can add to it here ..
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topHeadlinesHealthResponse ?: resultResponse)
            }
        } else {
            currentKey = API_KEY2
            handleHeadlinesNewsHealthResponse(response)
        }
        return Resource.Error(response.message())
    }

    // Entertainment ..
    private fun handleHeadlinesNewsEntertainmentResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // first ingress the page number ..
                topHeadlinesPageEntertainmentPage++
                // check and set the topHeadlinesResponse ..
                if (topHeadlinesEntertainmentResponse == null) {
                    topHeadlinesEntertainmentResponse = resultResponse
                } else {
                    // if there is a response already .. I pass the articles from the newResponse to the oldResponse ..
                    val oldArticles = topHeadlinesEntertainmentResponse?.articles
                    val newArticles = resultResponse.articles
                    // i changes List<Article> to MutableList<Article> so i can add to it here ..
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topHeadlinesEntertainmentResponse ?: resultResponse)
            }
        } else {
            currentKey = API_KEY2
            handleHeadlinesNewsEntertainmentResponse(response)
        }
        return Resource.Error(response.message())
    }

    // General ..
    fun handleHeadlinesNewsGeneralResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // first ingress the page number ..
                topHeadlinesPageGeneralPage++
                // check and set the topHeadlinesResponse ..
                if (topHeadlinesGeneralResponse == null) {
                    topHeadlinesGeneralResponse = resultResponse
                } else {
                    // if there is a response already .. I pass the articles from the newResponse to the oldResponse ..
                    val oldArticles = topHeadlinesGeneralResponse?.articles
                    val newArticles = resultResponse.articles
                    // i changes List<Article> to MutableList<Article> so i can add to it here ..
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topHeadlinesGeneralResponse ?: resultResponse)
            }
        } else {
            currentKey = API_KEY2
            handleHeadlinesNewsGeneralResponse(response)
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
        } else {
            currentKey = API_KEY2
            handleSearchNewsResponse(response)
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


    // Technology ..
    suspend fun safeTopHeadlinesNewsTechnologyCall(countryCode: String, category: String = "") {
        topHeadlineNewsTechnology.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getToHeadlinesNewsWithCategory(
                    countryCode,
                    category,
                    topHeadlinesPageTechnologyPage
                )
                topHeadlineNewsTechnology.postValue(handleHeadlinesNewsTechnologyResponse(response))
            } else {
                topHeadlineNewsTechnology.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                // topHeadlines function could also cause an exception this why I need when here ..
                is IOException -> topHeadlineNewsTechnology.postValue(Resource.Error("Network Failure"))
                else -> topHeadlineNewsTechnology.postValue(Resource.Error("Another Error not IOException"))
            }
        }
    }

    // Sports ..
    private suspend fun safeTopHeadlinesNewsSportsCall(countryCode: String, category: String) {
        topHeadlineNewsSports.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getToHeadlinesNewsWithCategory(
                    countryCode,
                    category,
                    topHeadlinesPageSportsPage
                )
                topHeadlineNewsSports.postValue(handleHeadlinesNewsSportsResponse(response))
            } else {
                topHeadlineNewsSports.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                // topHeadlines function could also cause an exception this why I need when here ..
                is IOException -> topHeadlineNewsSports.postValue(Resource.Error("Network Failure"))
                else -> topHeadlineNewsSports.postValue(Resource.Error("Another Error not IOException"))
            }
        }
    }

    // Science ..
    private suspend fun safeTopHeadlinesNewsScienceCall(countryCode: String, category: String) {
        topHeadlineNewsScience.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getToHeadlinesNewsWithCategory(
                    countryCode,
                    category,
                    topHeadlinesPageSciencePage
                )
                topHeadlineNewsScience.postValue(handleHeadlinesNewsScienceResponse(response))
            } else {
                topHeadlineNewsScience.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                // topHeadlines function could also cause an exception this why I need when here ..
                is IOException -> topHeadlineNewsScience.postValue(Resource.Error("Network Failure"))
                else -> topHeadlineNewsScience.postValue(Resource.Error("Another Error not IOException"))
            }
        }
    }

    // Business ..
    private suspend fun safeTopHeadlinesNewsBusinessCall(countryCode: String, category: String) {
        topHeadlineNewsBusiness.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getToHeadlinesNewsWithCategory(
                    countryCode,
                    category,
                    topHeadlinesPageBusinessPage
                )
                topHeadlineNewsBusiness.postValue(handleHeadlinesNewsBusinessResponse(response))
            } else {
                topHeadlineNewsBusiness.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                // topHeadlines function could also cause an exception this why I need when here ..
                is IOException -> topHeadlineNewsBusiness.postValue(Resource.Error("Network Failure"))
                else -> topHeadlineNewsBusiness.postValue(Resource.Error("Another Error not IOException"))
            }
        }
    }

    // Health ..
    private suspend fun safeTopHeadlinesNewsHealthCall(countryCode: String, category: String) {
        topHeadlineNewsHealth.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getToHeadlinesNewsWithCategory(
                    countryCode,
                    category,
                    topHeadlinesPageHealthPage
                )
                topHeadlineNewsHealth.postValue(handleHeadlinesNewsHealthResponse(response))
            } else {
                topHeadlineNewsHealth.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                // topHeadlines function could also cause an exception this why I need when here ..
                is IOException -> topHeadlineNewsHealth.postValue(Resource.Error("Network Failure"))
                else -> topHeadlineNewsHealth.postValue(Resource.Error("Another Error not IOException"))
            }
        }
    }

    // Entertainment ..
    private suspend fun safeTopHeadlinesNewsEntertainmentCall(
        countryCode: String,
        category: String
    ) {
        topHeadlineNewsEntertainment.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getToHeadlinesNewsWithCategory(
                    countryCode,
                    category,
                    topHeadlinesPageEntertainmentPage
                )
                topHeadlineNewsEntertainment.postValue(
                    handleHeadlinesNewsEntertainmentResponse(
                        response
                    )
                )
            } else {
                topHeadlineNewsEntertainment.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                // topHeadlines function could also cause an exception this why I need when here ..
                is IOException -> topHeadlineNewsEntertainment.postValue(Resource.Error("Network Failure"))
                else -> topHeadlineNewsEntertainment.postValue(Resource.Error("Another Error not IOException"))
            }
        }
    }

    // General ..
    private suspend fun safeTopHeadlinesNewsGeneralCall(countryCode: String, category: String) {
        topHeadlineNewsGeneral.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getToHeadlinesNewsWithCategory(
                    countryCode,
                    category,
                    topHeadlinesPageEntertainmentPage
                )
                topHeadlineNewsGeneral.postValue(handleHeadlinesNewsGeneralResponse(response))
            } else {
                topHeadlineNewsGeneral.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                // topHeadlines function could also cause an exception this why I need when here ..
                is IOException -> topHeadlineNewsGeneral.postValue(Resource.Error("Network Failure"))
                else -> topHeadlineNewsGeneral.postValue(Resource.Error("Another Error not IOException"))
            }
        }
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
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(app.applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}