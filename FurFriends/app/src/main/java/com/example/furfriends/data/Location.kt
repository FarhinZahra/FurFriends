package com.example.furfriends.data

import com.google.firebase.firestore.DocumentId

// This data class represents a single Location document in our Firestore database.
// The @DocumentId annotation tells Firestore to automatically fill the `id` field
// with the document's unique ID.
data class Location(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val city: String = "",
    val address: String = "",
    val distance: String = "", // This could be calculated on the client side later
    val imageUrl: String = ""
)
