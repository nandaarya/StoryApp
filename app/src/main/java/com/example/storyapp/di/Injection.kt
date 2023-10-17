package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.Repository
import com.example.storyapp.data.datastore.LocaleDataStore
import com.example.storyapp.data.datastore.UserPreference
import com.example.storyapp.data.datastore.dataStore
import com.example.storyapp.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val locale = LocaleDataStore.getInstance(context.dataStore)
        return Repository.getInstance(apiService, pref, locale)
    }
}