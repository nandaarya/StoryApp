package com.example.storyapp.di

import android.content.Context
import android.util.Log
import com.example.storyapp.data.Repository
import com.example.storyapp.data.datastore.UserPreference
import com.example.storyapp.data.datastore.dataStore
import com.example.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService, pref)
    }
}