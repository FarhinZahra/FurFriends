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

    private val _actionStatus = MutableLiveData<Result<Unit>>()
    val actionStatus: LiveData<Result<Unit>> = _actionStatus

    private val _postsCount = MutableLiveData<Int>()
    val postsCount: LiveData<Int> = _postsCount

    private val _requestsCount = MutableLiveData<Int>()
    val requestsCount: LiveData<Int> = _requestsCount

    private val _favoritesCount = MutableLiveData<Int>()
    val favoritesCount: LiveData<Int> = _favoritesCount

    fun loadProfile() {
        viewModelScope.launch {
            repository.getUserProfile().onSuccess { user ->
                _userProfile.postValue(user)
            }.onFailure {
                // Handle error
            }

            repository.getMyPets().onSuccess { pets ->
                _myPets.postValue(pets)
                _postsCount.postValue(pets.size)
            }.onFailure {
                // Handle error
            }

            repository.getRequestsCount().onSuccess { count ->
                _requestsCount.postValue(count)
            }.onFailure {
                // Handle error
            }

            repository.getFavoritesCount().onSuccess { count ->
                _favoritesCount.postValue(count)
            }.onFailure {
                // Handle error
            }
        }
    }

    fun markPetAdopted(petId: String) {
        viewModelScope.launch {
            val result = repository.updatePetStatus(petId, "adopted")
            _actionStatus.postValue(result)
        }
    }

    fun deletePet(petId: String) {
        viewModelScope.launch {
            val result = repository.deletePet(petId)
            _actionStatus.postValue(result)
        }
    }
}
