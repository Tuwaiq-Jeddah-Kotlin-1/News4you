package com.tuwaiq.newsplanet.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tuwaiq.newsplanet.repo.NewsRepo


//this class is to define how my ViewModel should be created ..
class NewsViewModelProviderFactory(val app : Application , val newsRepo: NewsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app , newsRepo) as T
    }
}