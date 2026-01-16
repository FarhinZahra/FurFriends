package com.example.furfriends.data

import com.google.firebase.firestore.DocumentId

// This data class represents a single Pet document in our Firestore database.
data class Pet(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val details: String = "", // e.g., "3 years, Golden, 20kg"
    val imageUrl: String = "",
    val location: String = "",
    val story: String = "",
    val ownerId: String = "" // The UID of the user who uploaded the pet
)
