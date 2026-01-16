package com.example.furfriends.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.Pet
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val repository = FavoritesRepository()

    private val _favoritePets = MutableLiveData<List<Pet>>()
    val favoritePets: LiveData<List<Pet>> = _favoritePets

    private val _toggleStatus = MutableLiveData<Result<Unit>>()
    val toggleStatus: LiveData<Result<Unit>> = _toggleStatus

    fun loadFavoritePets() {
        viewModelScope.launch {
            val result = repository.getFavoritePets()
            result.onSuccess { petList ->
                _favoritePets.postValue(petList)
            }
            result.onFailure {
                // Handle the error here if needed
            }
        }
    }

    fun toggleFavorite(petId: String) {
        viewModelScope.launch {
            val result = repository.toggleFavorite(petId)
            _toggleStatus.postValue(result)
        }
    }
}
