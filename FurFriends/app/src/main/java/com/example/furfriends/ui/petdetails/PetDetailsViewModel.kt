package com.example.furfriends.ui.petdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.Pet
import kotlinx.coroutines.launch

class PetDetailsViewModel : ViewModel() {

    private val repository = PetDetailsRepository()

    private val _petDetails = MutableLiveData<Pet>()
    val petDetails: LiveData<Pet> = _petDetails

    fun loadPetDetails(petId: String) {
        viewModelScope.launch {
            repository.getPetDetails(petId).onSuccess {
                _petDetails.postValue(it)
            }.onFailure {
                // Handle error
            }
        }
    }
}
