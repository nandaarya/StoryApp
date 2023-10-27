package com.example.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.Repository
import com.example.storyapp.data.Result
import com.example.storyapp.data.UserModel
import com.example.storyapp.data.response.ListStoryItem

class MapsViewModel(private val repository: Repository): ViewModel() {
    private val _storyListWithLocation = MediatorLiveData<Result<List<ListStoryItem>>>()
    val storyListWithLocation: LiveData<Result<List<ListStoryItem>>> = _storyListWithLocation

    fun getStoriesWithLocation(token: String) {
        val liveData = repository.getStoriesWithLocation(token)
        _storyListWithLocation.addSource(liveData) { result ->
            _storyListWithLocation.value = result
        }
    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}