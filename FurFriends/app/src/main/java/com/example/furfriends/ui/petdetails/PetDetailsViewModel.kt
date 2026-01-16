package com.example.furfriends.ui.petdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.Pet
import com.example.furfriends.data.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class PetDetailsViewModel : ViewModel() {

    private val repository = PetDetailsRepository()

    private val _petDetails = MutableLiveData<Pet>()
    val petDetails: LiveData<Pet> = _petDetails

    private val _ownerDetails = MutableLiveData<User>()
    val ownerDetails: LiveData<User> = _ownerDetails

    private val _requestStatus = MutableLiveData<String?>()
    val requestStatus: LiveData<String?> = _requestStatus

    fun loadPetDetails(petId: String) {
        viewModelScope.launch {
            repository.getPetDetails(petId).onSuccess {
                _petDetails.postValue(it)
                if (it.ownerId.isNotBlank()) {
                    repository.getOwnerDetails(it.ownerId).onSuccess { owner ->
                        _ownerDetails.postValue(owner)
                    }.onFailure {
                        // Handle error
                    }
                }
                val requesterId = FirebaseAuth.getInstance().currentUser?.uid
                if (!requesterId.isNullOrBlank()) {
                    repository.getRequestStatus(petId, requesterId).onSuccess { status ->
                        _requestStatus.postValue(status)
                    }.onFailure {
                        // Handle error
                    }
                }
            }.onFailure {
                // Handle error
            }
        }
    }
}
