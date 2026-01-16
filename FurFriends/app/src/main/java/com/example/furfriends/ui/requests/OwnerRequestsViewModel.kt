package com.example.furfriends.ui.requests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.AdoptionRequest
import kotlinx.coroutines.launch

class OwnerRequestsViewModel : ViewModel() {

    private val repository = OwnerRequestsRepository()

    private val _requests = MutableLiveData<List<AdoptionRequest>>()
    val requests: LiveData<List<AdoptionRequest>> = _requests

    private val _actionStatus = MutableLiveData<Result<Unit>>()
    val actionStatus: LiveData<Result<Unit>> = _actionStatus

    fun loadRequests() {
        viewModelScope.launch {
            repository.getRequestsForOwner().onSuccess { list ->
                _requests.postValue(list)
            }.onFailure {
                _requests.postValue(emptyList())
            }
        }
    }

    fun approve(request: AdoptionRequest) {
        viewModelScope.launch {
            val result = repository.approveRequest(request)
            _actionStatus.postValue(result)
        }
    }

    fun reject(request: AdoptionRequest) {
        viewModelScope.launch {
            val result = repository.rejectRequest(request)
            _actionStatus.postValue(result)
        }
    }
}
