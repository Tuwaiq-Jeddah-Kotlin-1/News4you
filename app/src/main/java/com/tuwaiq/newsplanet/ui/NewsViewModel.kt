package com.tuwaiq.newsplanet.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuwaiq.newsplanet.models.NewsResponse
import com.tuwaiq.newsplanet.repo.NewsRepo
import com.tuwaiq.newsplanet.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepo: NewsRepo) : ViewModel() {

    // LiveData object ..
    val topHeadlineNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    // I declare the page number here in the viewModel cuz if it's in the fragment it will reset with any change ..
    val topHeadlinesPage = 1

    init {
        getTopHeadlines("us")
    }

    // this is a coroutines function I used with viewModelScope that will stay alive as long as this viewModel alive ..
    fun getTopHeadlines(countryCode: String) = viewModelScope.launch {
        topHeadlineNews.postValue(Resource.Loading())
        val response = newsRepo.getBreakingNews(countryCode, topHeadlinesPage)
        topHeadlineNews.postValue(handleHeadlinesNewsResponse(response))
    }


    // in this function I check if the response is successful or not and send the message to the Resource ..
    private fun handleHeadlinesNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}