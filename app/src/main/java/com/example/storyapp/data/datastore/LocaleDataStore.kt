package com.example.storyapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocaleDataStore private constructor(private val dataStore: DataStore<Preferences>) {
    fun getLocaleSetting(): Flow<String> {
        return dataStore.data.map {
            it[LOCALE_KEY] ?: "en"
        }
    }

    suspend fun saveLocaleSetting(localeName: String) {
        dataStore.edit {
            it[LOCALE_KEY] = localeName
        }
    }

    companion object {
        private val LOCALE_KEY = stringPreferencesKey("locale")

        @Volatile
        private var INSTANCE: LocaleDataStore? = null
        fun getInstance(dataStore: DataStore<Preferences>): LocaleDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = LocaleDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
