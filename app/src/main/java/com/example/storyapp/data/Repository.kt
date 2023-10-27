package com.example.storyapp.data

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.datastore.LocaleDataStore
import com.example.storyapp.data.datastore.UserPreference
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.UploadStoryResponse
import com.example.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val localeDataStore: LocaleDataStore
) {

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                val token = response.loginResult.token
                saveSession(UserModel(email, token))
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getStories(token: String): LiveData<Result<List<ListStoryItem>>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.getStories("Bearer $token")
                Log.d("paging", "token: $token")
                val storyList = response.listStory
                emit(Result.Success(storyList))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getStoriesWithLocation(token: String): LiveData<Result<List<ListStoryItem>>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.getStoriesWithLocation("Bearer $token")
                val storyList = response.listStory
                emit(Result.Success(storyList))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        currentLocation: Location?
    ): LiveData<Result<UploadStoryResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = if (currentLocation != null) {
                    apiService.uploadStory(
                        "Bearer $token",
                        file,
                        description,
                        currentLocation.latitude.toString()
                            .toRequestBody("text/plain".toMediaType()),
                        currentLocation.longitude.toString()
                            .toRequestBody("text/plain".toMediaType())
                    )
                } else {
                    apiService.uploadStory("Bearer $token", file, description)
                }
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getLocale(): Flow<String> = localeDataStore.getLocaleSetting()

    suspend fun saveLocale(locale: String) {
        localeDataStore.saveLocaleSetting(locale)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            localeDataStore: LocaleDataStore
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference, localeDataStore)
            }.also { instance = it }
    }
}