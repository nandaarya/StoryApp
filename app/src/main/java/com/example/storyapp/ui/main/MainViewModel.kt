package com.example.storyapp.ui.main

import androidx.lifecycle.*
import com.example.storyapp.data.UserModel
import com.example.storyapp.data.Repository
import com.example.storyapp.data.response.ListStoryItem
import kotlinx.coroutines.launch
import com.example.storyapp.data.Result

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _storyList = MediatorLiveData<Result<List<ListStoryItem>>>()
    val storyList: LiveData<Result<List<ListStoryItem>>> = _storyList

    fun getStories(token: String) {
        val liveData = repository.getStories(token)
        _storyList.addSource(liveData) { result ->
            _storyList.value = result
        }
    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}