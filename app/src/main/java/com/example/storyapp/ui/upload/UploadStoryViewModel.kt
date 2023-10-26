package com.example.storyapp.ui.upload

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.Repository
import com.example.storyapp.data.Result
import com.example.storyapp.data.UserModel
import com.example.storyapp.data.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(private val repository: Repository): ViewModel() {

    private val _uploadStoryResponse = MediatorLiveData<Result<UploadStoryResponse>>()
    val uploadStoryResponse: LiveData<Result<UploadStoryResponse>> = _uploadStoryResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        currentLocation: Location?
    ) {
        val liveData = repository.uploadStory(token, file, description, currentLocation)
        _uploadStoryResponse.addSource(liveData) { result ->
            _uploadStoryResponse.value = result
        }
    }
}