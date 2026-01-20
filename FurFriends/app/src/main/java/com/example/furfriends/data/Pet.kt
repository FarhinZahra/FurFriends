package com.example.furfriends.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

// This data class represents a single Pet document in our Firestore database.
data class Pet(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val age: String = "",
    val breed: String = "",
    val type: String = "", // Dog/Cat/Other
    val weight: String = "",
    val story: String = "",
    val imageUrls: List<String> = emptyList(),
    val locationId: String = "",
    val locationName: String = "",
    val ownerId: String = "",
    val status: String = "available",
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    fun displayDetails(): String = listOf(age, breed, weight).filter { it.isNotBlank() }.joinToString(", ")
}
