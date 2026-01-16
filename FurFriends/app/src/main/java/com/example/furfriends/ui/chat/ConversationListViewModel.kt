package com.example.furfriends.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furfriends.data.chat.ChatConversation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ConversationListViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _conversations = MutableLiveData<List<ChatConversation>>()
    val conversations: LiveData<List<ChatConversation>> = _conversations

    fun loadConversations(userId: String) {
        viewModelScope.launch {
            repository.getConversations(userId).collect {
                _conversations.postValue(it)
            }
        }
    }
}
