package com.example.furfriends.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.auth.AuthRepository
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _registrationStatus = MutableLiveData<Result<Unit>>()
    val registrationStatus: LiveData<Result<Unit>> = _registrationStatus

    fun registerUser(name: String, email: String, phone: String, address: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.registerUser(name, email, phone, address, password)
            _registrationStatus.postValue(result)
        }
    }
}
