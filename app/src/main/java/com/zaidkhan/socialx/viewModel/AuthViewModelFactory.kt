package com.zaidkhan.socialx.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaidkhan.socialx.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class AuthViewModelFactory @Inject constructor(@Named("Auth") private val repository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}