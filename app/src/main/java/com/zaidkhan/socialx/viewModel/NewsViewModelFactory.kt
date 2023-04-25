package com.zaidkhan.socialx.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaidkhan.socialx.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Named

class NewsViewModelFactory @Inject constructor(@Named("News") private val repository: NewsRepository,private val appContext: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(repository,appContext) as T
    }
}