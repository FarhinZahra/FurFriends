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

    private val _requestStatuses = MutableLiveData<Map<String, String>>()
    val requestStatuses: LiveData<Map<String, String>> = _requestStatuses

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var allPets: List<Pet> = emptyList()
    private var activeFilter: String = "All"

    fun loadFeed() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            repository.getFeedPets().onSuccess { pets ->
                allPets = pets
                applyFilter()
            }.onFailure {
                allPets = emptyList()
                applyFilter()
            }
            _isLoading.postValue(false)
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

    fun loadRequestStatuses() {
        viewModelScope.launch {
            repository.getMyRequestStatuses().onSuccess { map ->
                _requestStatuses.postValue(map)
            }.onFailure {
                _requestStatuses.postValue(emptyMap())
            }
        }
    }

    fun setFilter(filter: String) {
        activeFilter = filter
        applyFilter()
    }

    private fun applyFilter() {
        val filtered = when (activeFilter) {
            "Dog" -> allPets.filter { it.type.equals("Dog", ignoreCase = true) }
            "Cat" -> allPets.filter { it.type.equals("Cat", ignoreCase = true) }
            "Other" -> allPets.filter {
                it.type.isNotBlank() && !it.type.equals("Dog", ignoreCase = true) && !it.type.equals("Cat", ignoreCase = true)
            }
            else -> allPets
        }
        _feedPets.postValue(filtered)
    }

    fun toggleFavorite(petId: String) {
        viewModelScope.launch {
            val result = favoritesRepository.toggleFavorite(petId)
            _favoriteStatus.postValue(result)
        }
    }
}
