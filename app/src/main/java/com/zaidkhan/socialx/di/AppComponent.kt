package com.zaidkhan.socialx.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.zaidkhan.socialx.view.activity.MainActivity
import com.zaidkhan.socialx.view.activity.NewsActivity
import com.zaidkhan.socialx.view.fragment.LoginFragment
import com.zaidkhan.socialx.view.fragment.SignupFragment
import com.zaidkhan.socialx.viewModel.AuthViewModelFactory
import com.zaidkhan.socialx.viewModel.NewsViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,NetworkModule::class])
interface AppComponent {

    fun inject(newsActivity: NewsActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(signupFragment: SignupFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}