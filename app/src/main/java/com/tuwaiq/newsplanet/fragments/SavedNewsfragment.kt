package com.tuwaiq.newsplanet.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel

class SavedNewsfragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // to access the activity's ViewModel
        viewModel = (activity as NewsActivity).viewModel
    }
}