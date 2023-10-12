package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.datastore.UserPreference
import com.example.storyapp.data.datastore.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}