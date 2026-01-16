package com.example.furfriends.ui.uploadpet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.Pet
import kotlinx.coroutines.launch

class UploadPetViewModel : ViewModel() {

    private val repository = UploadPetRepository()

    private val _uploadStatus = MutableLiveData<Result<Unit>>()
    val uploadStatus: LiveData<Result<Unit>> = _uploadStatus

    fun uploadPet(pet: Pet) {
        viewModelScope.launch {
            val result = repository.uploadPet(pet)
            _uploadStatus.postValue(result)
        }
    }
}
