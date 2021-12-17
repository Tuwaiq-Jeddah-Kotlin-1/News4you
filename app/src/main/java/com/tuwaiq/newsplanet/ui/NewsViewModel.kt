package com.tuwaiq.newsplanet.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuwaiq.newsplanet.models.Article
import com.tuwaiq.newsplanet.models.NewsResponse
import com.tuwaiq.newsplanet.repo.NewsRepo
import com.tuwaiq.newsplanet.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepo: NewsRepo) : ViewModel() {

    // LiveData object ..
    val topHeadlineNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    // I declare the page number here in the viewModel cuz if it's in the fragment it will reset with any change ..
    var topHeadlinesPage = 1
    var searchNewsPage = 1


    // this is saved here to handle the response if the activity or fragment changed .. like rotate for example ..
    var topHeadlinesResponse : NewsResponse? = null
    var searchNewsResponse : NewsResponse? = null


    init {
        getTopHeadlines("us")
    }

    // this is a coroutines function I used with viewModelScope that will stay alive as long as this viewModel alive ..
    fun getTopHeadlines(countryCode: String) = viewModelScope.launch {
        topHeadlineNews.postValue(Resource.Loading())
        val response = newsRepo.getBreakingNews(countryCode, topHeadlinesPage)
        topHeadlineNews.postValue(handleHeadlinesNewsResponse(response))
    }

    fun searchNews(searchQuery : String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        var response = newsRepo.searchNews(searchQuery,searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }


    // in this function I check if the response is successful or not and send the message to the Resource ..
    private fun handleHeadlinesNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // first ingress the page number ..
                topHeadlinesPage++
                // check and set the topHeadlinesResponse ..
                if(topHeadlinesResponse == null){
                    topHeadlinesResponse = resultResponse
                }else{
                    // if there is a response already .. I pass the articles from the newResponse to the oldResponse ..
                    val oldArticles = topHeadlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    // i changes List<Article> to MutableList<Article> so i can add to it here ..
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topHeadlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // first ingress the page number ..
                searchNewsPage++
                // check and set the searchNewsResponse ..
                if(searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                }else{
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
}