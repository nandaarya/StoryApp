package com.example.storyapp.ui.main

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.UserModel
import com.example.storyapp.data.Repository
import com.example.storyapp.data.response.ListStoryItem
import kotlinx.coroutines.launch
import com.example.storyapp.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class MainViewModel(private val repository: Repository) : ViewModel() {

//    private val _storyList = MediatorLiveData<PagingData<ListStoryItem>>()
//    val storyList: LiveData<PagingData<ListStoryItem>> = _storyList

//    fun getStories(token: String) {
//        repository.getStories(token).cachedIn(viewModelScope)
//    }

    private var token: String = "token default"

    init {
       viewModelScope.launch {
           repository.getSession().collect{ user ->
               token = user.token
               Log.d("paging view model", "token: $token")
           }
       }
    }

//    fun getToken(): String {
//        token = repository.getSession().map { userModel ->
//            userModel.token
//        }
//        Log.d("paging view model", "token: $token")
//        return token
//    }

    val storyList: LiveData<PagingData<ListStoryItem>> =
        repository.getStories(token).cachedIn(viewModelScope)

//    fun getStories(token: String) {
//        val liveData = repository.getStories(token).cachedIn(viewModelScope)
//        Log.d("paging","data stories: $liveData")
//        _storyList.addSource(liveData) { result ->
//            _storyList.value = result
//        }
//    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}