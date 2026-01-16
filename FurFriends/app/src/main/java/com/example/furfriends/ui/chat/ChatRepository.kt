package com.example.furfriends.ui.chat

import com.example.furfriends.data.chat.ChatConversation
import com.example.furfriends.data.chat.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ChatRepository {

    private val db = FirebaseFirestore.getInstance()

    // This will fetch the list of conversations for the current user.
    // For now, it returns a hardcoded list for demonstration.
    suspend fun getConversations(userId: String): Flow<List<ChatConversation>> = flow {
        // In a real app, you would query Firestore for conversations where `participants` contains the userId
        val sampleConversations = listOf(
            ChatConversation(conversationId = "1", participants = listOf("user1", "user2"), lastMessage = "Hey, is Gary still available?"),
            ChatConversation(conversationId = "2", participants = listOf("user1", "user3"), lastMessage = "Yes, Pamuk is very friendly!")
        )
        emit(sampleConversations)
    }

    // This will fetch the messages for a specific conversation.
    // We will implement the real-time version of this later.
    suspend fun getMessages(conversationId: String): Flow<List<ChatMessage>> = flow {
        val sampleMessages = listOf(
            ChatMessage(senderId = "user2", message = "Hey, is Gary still available?"),
            ChatMessage(senderId = "user1", message = "He is! Would you like to meet him?")
        )
        emit(sampleMessages)
    }

    // This will send a new message to a conversation.
    suspend fun sendMessage(conversationId: String, message: ChatMessage): Result<Unit> {
        return try {
            db.collection("chats").document(conversationId).collection("messages").add(message).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
