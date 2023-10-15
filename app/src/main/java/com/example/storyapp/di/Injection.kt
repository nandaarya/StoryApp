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
        val user = runBlocking { pref.getSession().first() }
        Log.d("story list", "Token di Injection: ${user.token}")
        val apiService = ApiConfig.getApiService(user.token)
        return Repository.getInstance(apiService, pref)
    }
}