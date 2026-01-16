package com.example.furfriends.ui.locationdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.Location
import com.example.furfriends.data.Pet
import kotlinx.coroutines.launch

class LocationDetailsViewModel : ViewModel() {

    private val repository = LocationDetailsRepository()

    private val _locationDetails = MutableLiveData<Location>()
    val locationDetails: LiveData<Location> = _locationDetails

    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> = _pets

    fun loadLocationDetails(locationId: String) {
        viewModelScope.launch {
            repository.getLocationDetails(locationId).onSuccess {
                _locationDetails.postValue(it)
            }.onFailure {
                // Handle error
            }

            repository.getPetsForLocation(locationId).onSuccess {
                _pets.postValue(it)
            }.onFailure {
                // Handle error
            }
        }
    }
}
