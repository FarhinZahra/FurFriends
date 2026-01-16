package com.example.furfriends.data.chat

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ChatConversation(
    val conversationId: String = "",
    val participants: List<String> = emptyList(), // List of user UIDs
    val lastMessage: String = "",
    @ServerTimestamp
    val lastMessageTimestamp: Date? = null,
    // We can add other useful info here later, like the petId
)
