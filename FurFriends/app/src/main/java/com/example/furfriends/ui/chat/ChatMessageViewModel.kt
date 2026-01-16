package com.example.furfriends.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.chat.ChatMessage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatMessageViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _sendStatus = MutableLiveData<Result<Unit>>()
    val sendStatus: LiveData<Result<Unit>> = _sendStatus

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            repository.getMessages(conversationId).collect {
                _messages.postValue(it)
            }
        }
    }

    fun sendMessage(conversationId: String, message: ChatMessage) {
        viewModelScope.launch {
            val result = repository.sendMessage(conversationId, message)
            _sendStatus.postValue(result)
        }
    }
}
