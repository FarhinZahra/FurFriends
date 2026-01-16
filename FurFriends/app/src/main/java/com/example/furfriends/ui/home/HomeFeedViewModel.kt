package com.example.furfriends.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.Pet
import com.example.furfriends.ui.favorites.FavoritesRepository
import kotlinx.coroutines.launch

class HomeFeedViewModel : ViewModel() {

    private val repository = HomeFeedRepository()
    private val favoritesRepository = FavoritesRepository()

    private val _feedPets = MutableLiveData<List<Pet>>()
    val feedPets: LiveData<List<Pet>> = _feedPets

    private val _favoriteStatus = MutableLiveData<Result<Unit>>()
    val favoriteStatus: LiveData<Result<Unit>> = _favoriteStatus

    private val _favoriteIds = MutableLiveData<Set<String>>()
    val favoriteIds: LiveData<Set<String>> = _favoriteIds

    fun loadFeed() {
        viewModelScope.launch {
            repository.getFeedPets().onSuccess { pets ->
                _feedPets.postValue(pets)
            }.onFailure {
                _feedPets.postValue(emptyList())
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            favoritesRepository.getFavoriteIds().onSuccess { ids ->
                _favoriteIds.postValue(ids)
            }.onFailure {
                _favoriteIds.postValue(emptySet())
            }
        }
    }

    fun toggleFavorite(petId: String) {
        viewModelScope.launch {
            val result = favoritesRepository.toggleFavorite(petId)
            _favoriteStatus.postValue(result)
        }
    }
}
