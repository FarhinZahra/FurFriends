package com.example.furfriends.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.Location
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private val repository = LocationRepository()

    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>> = _locations

    // We can add a LiveData for errors later if needed
    // private val _error = MutableLiveData<String>()
    // val error: LiveData<String> = _error

    fun loadLocations() {
        viewModelScope.launch {
            val result = repository.getLocations()
            result.onSuccess { locationList ->
                _locations.postValue(locationList)
            }
            result.onFailure { exception ->
                // _error.postValue(exception.message)
            }
        }
    }
}
