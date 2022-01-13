package com.tuwaiq.newsplanet.models

import androidx.lifecycle.MutableLiveData


data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)