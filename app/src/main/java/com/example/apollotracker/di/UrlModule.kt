package com.example.apollotracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UrlModule {

    @Provides
    @Singleton
    fun provideUrl(): String = BASE_URL

    private const val BASE_URL = "https://api.coinpaprika.com/"
}