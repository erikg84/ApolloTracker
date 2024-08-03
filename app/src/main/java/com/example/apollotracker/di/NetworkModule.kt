package com.example.apollotracker.di

import com.example.apollotracker.remote.CoinPaprikaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCoinPaprikaApi(retrofit: Retrofit): CoinPaprikaApi {
        return retrofit.create(CoinPaprikaApi::class.java)
    }

    private const val BASE_URL = "https://api.coinpaprika.com/"
}

