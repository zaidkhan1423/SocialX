package com.zaidkhan.socialx.repository

import com.zaidkhan.socialx.model.NewsResponse
import com.zaidkhan.socialx.retrofit.NewsAPI
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsAPI: NewsAPI) {

    suspend fun getNews() = newsAPI.getNews()

    suspend fun getSearchNews(query: String, page: Int): Response<NewsResponse> {
        return newsAPI.getSearchNews(searchQuery = query, page = page)
    }

}