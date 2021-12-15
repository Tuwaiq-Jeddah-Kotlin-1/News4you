package com.tuwaiq.newsplanet.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.adapters.NewsAdapter
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel
import com.tuwaiq.newsplanet.util.Constants.Companion.SEARCH_QUERY_TIME_DELAY
import com.tuwaiq.newsplanet.util.Resource
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SeasrchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter : NewsAdapter
    val TAG = "SearchNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // to access the activity's ViewModel
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        // here I put the article in a bundle to pass it between the fragments ..
        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article" , article)
            }
            findNavController().navigate(
                R.id.action_seasrchNewsFragment_to_articleFragment,
                bundle
            )
        }

        // This coroutine job will help to delay the search act while typing the query 500 milli sec before attempt to search ..
        var job : Job? = null
        // Listener If the text changed or not .. If changed the job will canceled and not enter the scope ..
        // After entering the scope I check the delay first ..
        etSearch.addTextChangedListener { searchText ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_QUERY_TIME_DELAY)
                // here I start the search request .. after checking not null and null empty ..
                searchText?.let {
                    if (searchText.toString().isNotEmpty()){
                        viewModel.searchNews(searchText.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.mDiffer.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
