package com.tuwaiq.newsplanet.fragments



import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.adapters.NewsAdapter
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel
import com.tuwaiq.newsplanet.ui.bottomNavView
import com.tuwaiq.newsplanet.util.Constants.Companion.QUERY_PAGE_SIZE
import com.tuwaiq.newsplanet.util.Resource
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_top_headlines_news.*
import kotlinx.android.synthetic.main.fragment_top_headlines_news.paginationProgressBar


class TopHeadlineFragment(val type : String) : Fragment(R.layout.fragment_top_headlines_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        bottomNavView.visibility = View.VISIBLE

        viewModel.newCategory = type
        viewModel.getTopHeadlinesWithCategory("us", type , viewLifecycleOwner )



        viewModel.topHeadlineNewsWithCategory.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        /* Here I changed the articles from MutableList to a List cuz Deffer doesn't work with Mutable list perfectly ..
                        this solved the problem for me .. */
                        newsAdapter.mDiffer.submitList(newsResponse.articles)
                        // totalResults is How many results in the response .. +2 cuz last page is always empty and 1 for the rounding ..
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.topHeadlinesPageWithCategoryPage == totalPages
                        if (isLastPage) {
                            rvTopHeadlines.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


        setupRecyclerView()

        // here I put the article in a bundle to pass it between the fragments ..
        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article" , article)
            }
            findNavController().navigate(
                R.id.action_mainFragment_to_articleFragment,
                bundle
            )
        }



//        viewModel.topHeadlineNewsGeneral.observe(viewLifecycleOwner, Observer { response ->
//            when(response) {
//                is Resource.Success -> {
//                    hideProgressBar()
//                    response.data?.let { newsResponse ->
//                        /* Here I changed the articles from MutableList to a List cuz Deffer doesn't work with Mutable list perfectly ..
//                        this solved the problem for me .. */
//                        newsAdapter.mDiffer.submitList(newsResponse.articles.toList())
//                        // totalResults is How many results in the response .. +2 cuz last page is always empty and 1 for the rounding ..
//                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
//                        isLastPage = viewModel.topHeadlinesPageGeneralPage == totalPages
//                        if(isLastPage){
//                            rvTopHeadlines.setPadding(0,0,0,0)
//                        }
//                    }
//                }
//
//                is Resource.Error -> {
//                    hideProgressBar()
//                    response.message?.let { message ->
//                        Toast.makeText(activity , "An error occurred: $message" , Toast.LENGTH_SHORT).show()
//                    }
//                }
//                is Resource.Loading -> {
//                    showProgressBar()
//                }
//            }
//        })
    }



    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {

        // this function called when scrolling .. and calculate if the user reach the bottom of the recycler or not using the layoutManager ..
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            // using these three numbers I'll be able to calculate if the user in the bottom of the recycler ..

            // This variable means the "position of the first item visible in the screen at this moment" ..
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            // this variable means "How many Items are visible now" ..
            val visibleItemCount = layoutManager.childCount
            // this variable means "How many item in the recycler ..
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage

            // If this variable true this means the LastItem is visible ..
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            // this variable detect if we scrolled down or not yet ..
            val isNotAtTheBeginning = firstVisibleItemPosition >= 0
            // this variable is to check if there are items in recycler more than elements in the query page ..
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            // this variable is to check if the paging should happen or not yet ..
            val shouldPaging = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtTheBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaging){
                viewModel.getTopHeadlinesWithCategory("us" , type,viewLifecycleOwner)
                isScrolling = false
            }
        }

        // Here is to check if the user is scrolling or not ..
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvTopHeadlines.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@TopHeadlineFragment.scrollListener)
        }
    }


}