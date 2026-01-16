package com.example.furfriends.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.auth.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _loginStatus = MutableLiveData<Result<Unit>>()
    val loginStatus: LiveData<Result<Unit>> = _loginStatus

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.loginUser(email, password)
            _loginStatus.postValue(result)
        }
    }
}
