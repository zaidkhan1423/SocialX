package com.zaidkhan.socialx.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaidkhan.socialx.utils.NewsResource
import com.zaidkhan.socialx.utils.ConnectionManager
import com.zaidkhan.socialx.model.NewsResponse
import com.zaidkhan.socialx.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class NewsViewModel @Inject constructor(
    @Named("News") private val repository: NewsRepository,
    private val appContext: Application
) : ViewModel() {

    private val _newsData: MutableLiveData<NewsResource<NewsResponse>> = MutableLiveData()
    val newsData: LiveData<NewsResource<NewsResponse>> = _newsData
    private val newsDataTemp = MutableLiveData<NewsResource<NewsResponse>>()

    private val _searchData: MutableLiveData<NewsResource<NewsResponse>> = MutableLiveData()
    val searchData: LiveData<NewsResource<NewsResponse>> = _searchData

    private var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getNews()
    }

    fun getNews() = viewModelScope.launch {
        fetchNews()
    }

    fun getSearchNews(searchQuery: String) = viewModelScope.launch {
        fetchSearchNews(searchQuery)
    }

    private suspend fun fetchNews() {
        _newsData.postValue(NewsResource.Loading())
        try {
            if (ConnectionManager.checkConnectivity(appContext)) {
                val response = repository.getNews()
                newsDataTemp.postValue(NewsResource.Success(response.body()!!))
                _newsData.postValue(handleNewsResponse(response))
            } else {
                _newsData.postValue(NewsResource.Error("No Internet Connection"))
                Toast.makeText(appContext, "No internet Connection", Toast.LENGTH_SHORT).show()
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _newsData.postValue(
                    NewsResource.Error(
                        t.message!!
                    )
                )
                else -> _newsData.postValue(
                    NewsResource.Error(
                        t.message!!
                    )
                )
            }
        }
    }

    private suspend fun fetchSearchNews(searchQuery: String) {
        _searchData.postValue(NewsResource.Loading())
        try {
            if (ConnectionManager.checkConnectivity(appContext)) {
                val response = repository.getSearchNews(searchQuery, searchNewsPage)
                _searchData.postValue(handleSearchNewsResponse(response))
            } else {
                _searchData.postValue(NewsResource.Error("No Internet Connection"))
                Toast.makeText(appContext, "No internet Connection", Toast.LENGTH_SHORT).show()
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchData.postValue(
                    NewsResource.Error(
                        t.message!!
                    )
                )
                else -> _searchData.postValue(
                    NewsResource.Error(
                        t.message!!
                    )
                )
            }
        }
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): NewsResource<NewsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                searchNewsResponse = if (searchNewsResponse == null) {
                    resultResponse
                } else {
                    resultResponse
                }
                return NewsResource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return NewsResource.Error(response.message())
    }
    private fun handleNewsResponse(response: Response<NewsResponse>): NewsResource<NewsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return NewsResource.Success(resultResponse)
            }
        }
        return NewsResource.Error(response.message())
    }
    fun onSearchClose() {
        _searchData.postValue(newsDataTemp.value)
    }
}