package com.example.fitnessapp.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.fitnessapp.api.Api
import com.example.fitnessapp.api.repositories.auth.AuthRepository
import com.example.fitnessapp.api.repositories.auth.AuthRepositoryImpl
import com.example.fitnessapp.api.repositories.userdata.DataRepository
import com.example.fitnessapp.api.repositories.userdata.DataRepositoryImpl
import com.example.fitnessapp.api.repositories.weight.UserWeightRepository
import com.example.fitnessapp.api.repositories.weight.UserWeightRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(): Api {
        return  Retrofit.Builder()
            .baseUrl("http://192.168.0.104:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: Api, prefs: SharedPreferences): AuthRepository {
        return AuthRepositoryImpl(api, prefs)
    }

    @Provides
    @Singleton
    fun provideUserDataRepository(api: Api, prefs: SharedPreferences): DataRepository {
        return DataRepositoryImpl(api, prefs)
    }

    @Provides
    @Singleton
    fun provideUserWeightRepository(api: Api, prefs: SharedPreferences): UserWeightRepository {
        return  UserWeightRepositoryImpl(api, prefs)
    }
}