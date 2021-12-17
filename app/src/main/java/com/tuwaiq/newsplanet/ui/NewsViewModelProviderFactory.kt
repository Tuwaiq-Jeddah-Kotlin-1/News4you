package com.tuwaiq.newsplanet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tuwaiq.newsplanet.repo.NewsRepo


//this class is to define how my ViewModel should be created .. NOW_FOR_TEST_ONLY
class NewsViewModelProviderFactory(val newsRepo: NewsRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepo) as T
    }
}