package com.zaidkhan.socialx.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zaidkhan.socialx.repository.AuthRepository
import com.zaidkhan.socialx.repository.AuthRepositoryImpl
import com.zaidkhan.socialx.repository.NewsRepository
import com.zaidkhan.socialx.viewModel.AuthViewModelFactory
import com.zaidkhan.socialx.viewModel.NewsViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Singleton
    @Named("Auth")
    @Provides
    fun provideAuthModelFactory(authRepository: AuthRepository): ViewModelProvider.Factory {
        return AuthViewModelFactory(authRepository)
    }

    @Singleton
    @Named("News")
    @Provides
    fun provideNewsModelFactory(newsRepository: NewsRepository,appContext: Application): ViewModelProvider.Factory {
        return NewsViewModelFactory(newsRepository,appContext)
    }

    @Singleton
    @Provides
    fun providesFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideGoogleSignInClient(application: Application): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("68452335559-nflddkmmn13fgd3sqqh9tr3e7no1rpno.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(application, gso)
    }

}