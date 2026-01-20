package com.example.furfriends.ui.pets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.Pet
import kotlinx.coroutines.launch

class PetEditViewModel : ViewModel() {

    private val repository = PetEditRepository()

    private val _pet = MutableLiveData<Pet>()
    val pet: LiveData<Pet> = _pet

    private val _updateStatus = MutableLiveData<Result<Unit>>()
    val updateStatus: LiveData<Result<Unit>> = _updateStatus

    fun loadPet(petId: String) {
        viewModelScope.launch {
            repository.getPet(petId).onSuccess { pet ->
                _pet.postValue(pet)
            }.onFailure {
                // Handle error
            }
        }
    }

    fun saveChanges(petId: String, story: String, status: String) {
        viewModelScope.launch {
            val result = repository.updatePet(petId, story, status)
            _updateStatus.postValue(result)
        }
    }
}
