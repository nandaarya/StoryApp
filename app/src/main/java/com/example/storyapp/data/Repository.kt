package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.datastore.UserPreference
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.UploadStoryResponse
import com.example.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
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
                Log.d("Response Register", response.message ?: "message null")
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
                Log.d("Response Login", "email: $email, token: $token")
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getStories(token: String): LiveData<Result<List<ListStoryItem>>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                Log.d("story list", "Mulai getStories")
                val response = apiService.getStories("Bearer $token")
                val storyList = response.listStory
                Log.d("story list", "${response.message}")
                emit(Result.Success(storyList))
            } catch (e: Exception) {
                Log.d("story list", "error getStories")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<Result<UploadStoryResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.uploadStory("Bearer $token", file, description)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }
    }
}