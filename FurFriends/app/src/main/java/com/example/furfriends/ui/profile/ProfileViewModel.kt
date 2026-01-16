package com.example.furfriends.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.Pet
import com.example.furfriends.data.User
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile

    private val _myPets = MutableLiveData<List<Pet>>()
    val myPets: LiveData<List<Pet>> = _myPets

    fun loadProfile() {
        viewModelScope.launch {
            repository.getUserProfile().onSuccess { user ->
                _userProfile.postValue(user)
            }.onFailure {
                // Handle error
            }

            repository.getMyPets().onSuccess { pets ->
                _myPets.postValue(pets)
            }.onFailure {
                // Handle error
            }
        }
    }
}
