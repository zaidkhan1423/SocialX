package com.zaidkhan.socialx.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaidkhan.socialx.SocialXApplication
import com.zaidkhan.socialx.adapter.NewsAdapter
import com.zaidkhan.socialx.utils.NewsResource
import com.zaidkhan.socialx.databinding.ActivityNewsBinding
import com.zaidkhan.socialx.viewModel.AuthViewModel
import com.zaidkhan.socialx.viewModel.NewsViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding

    @Inject
    @Named("Auth")
    lateinit var authfactory: ViewModelProvider.Factory
    private val authViewModel: AuthViewModel by viewModels { authfactory }

    @Inject
    @Named("News")
    lateinit var newsFactory: ViewModelProvider.Factory
    private val newsViewModel: NewsViewModel by viewModels { newsFactory }

    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as SocialXApplication).appComponent.inject(this)

        setSupportActionBar(binding.toolbar)

        adapter = NewsAdapter(this)

        init()

        newsViewModel.getNews()

        newsViewModel.newsData.observe(this, Observer { response ->
            when (response) {
                is NewsResource.Success -> {
                    adapter.submitList(response.data!!.articles)
                    Toast.makeText(this, "News Fetch Successfully", Toast.LENGTH_SHORT).show()
                }
                is NewsResource.Error -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
                is NewsResource.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchQuery = binding.etSearch.text.toString()
                newsViewModel.onSearchClose()
                MainScope().launch {
                    newsViewModel.getSearchNews(searchQuery)
                }
                newsViewModel.searchData.observe(this, Observer { response ->
                    when (response) {
                        is NewsResource.Success -> {
                            response.data.let { newsResponse ->
                                adapter.submitList(newsResponse?.articles?.toList())
                                Toast.makeText(this, "Showing Results", Toast.LENGTH_SHORT).show()
                            }
                        }
                        is NewsResource.Error -> {
                            response.message?.let { message ->
                                Toast.makeText(this, "Error!! $message", Toast.LENGTH_SHORT).show()
                            }
                        }

                        is NewsResource.Loading -> {
                            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                true
            } else {
                false
            }
        }
    }

    private fun init() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
    }

}