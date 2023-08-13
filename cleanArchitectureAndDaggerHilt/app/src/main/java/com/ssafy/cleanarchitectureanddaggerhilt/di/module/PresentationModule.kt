package com.ssafy.cleanarchitectureanddaggerhilt.di.module

import android.content.Context
import com.ssafy.cleanarchitectureanddaggerhilt.presentation.utils.CoroutineContextProvider
import com.ssafy.cleanarchitectureanddaggerhilt.presentation.utils.CoroutineContextProviderImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {
    @Provides
    @Singleton
    fun provideCoroutineContextProvider(contextProvider: CoroutineContextProviderImp): CoroutineContextProvider =
        contextProvider
}
