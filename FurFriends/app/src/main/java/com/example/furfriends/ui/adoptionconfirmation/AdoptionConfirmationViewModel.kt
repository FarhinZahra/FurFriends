package com.example.furfriends.ui.adoptionconfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.Pet
import com.example.furfriends.data.User
import kotlinx.coroutines.launch

class AdoptionConfirmationViewModel : ViewModel() {

    private val repository = AdoptionConfirmationRepository()

    private val _petDetails = MutableLiveData<Pet>()
    val petDetails: LiveData<Pet> = _petDetails

    private val _userDetails = MutableLiveData<User>()
    val userDetails: LiveData<User> = _userDetails

    fun loadConfirmationData(petId: String) {
        viewModelScope.launch {
            repository.getPetDetails(petId).onSuccess {
                _petDetails.postValue(it)
            }.onFailure {
                // Handle error
            }

            repository.getUserDetails().onSuccess {
                _userDetails.postValue(it)
            }.onFailure {
                // Handle error
            }
        }
    }
}
