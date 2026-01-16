package com.example.furfriends.ui.profileedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.User
import kotlinx.coroutines.launch

class ProfileEditViewModel : ViewModel() {

    private val repository = ProfileEditRepository()

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile

    private val _updateStatus = MutableLiveData<Result<Unit>>()
    val updateStatus: LiveData<Result<Unit>> = _updateStatus

    fun loadUserProfile() {
        viewModelScope.launch {
            val result = repository.getUserProfile()
            result.onSuccess { user ->
                _userProfile.postValue(user)
            }
            // We can handle the failure case here if needed
        }
    }

    fun updateUserProfile(user: User) {
        viewModelScope.launch {
            val result = repository.updateUserProfile(user)
            _updateStatus.postValue(result)
        }
    }
}
