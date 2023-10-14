package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Repository
import com.example.storyapp.data.Result
import com.example.storyapp.data.response.LoginResponse

class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val _loginResponse = MediatorLiveData<Result<LoginResponse>>()
    val loginResponse: LiveData<Result<LoginResponse>> = _loginResponse

    fun login(email: String, password: String) {
        val liveData = repository.login(email, password)
        _loginResponse.addSource(liveData) { result ->
            _loginResponse.value = result
        }
    }
}