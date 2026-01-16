package com.example.furfriends.ui.forgetpass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.auth.AuthRepository
import kotlinx.coroutines.launch

class ForgetPassViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _resetStatus = MutableLiveData<Result<Unit>>()
    val resetStatus: LiveData<Result<Unit>> = _resetStatus

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            _resetStatus.postValue(result)
        }
    }
}
