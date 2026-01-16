package com.example.furfriends.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class AdoptionRequest(
    @DocumentId
    val id: String = "",
    val petId: String = "",
    val petName: String = "",
    val ownerId: String = "",
    val requesterId: String = "",
    val requesterName: String = "",
    val requesterPhone: String = "",
    val status: String = "pending",
    val message: String = "",
    @ServerTimestamp
    val createdAt: Timestamp? = null
)
