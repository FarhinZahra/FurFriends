package com.example.furfriends.ui.favorites

import com.example.furfriends.data.Pet
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoritesRepository {

    private val db = FirebaseFirestore.getInstance()

    // This function now fetches the list of pets from your Cloud Firestore database.
    // In a real app, this would fetch only the pets favorited by the current user.
    suspend fun getFavoritePets(): Result<List<Pet>> {
        return try {
            val snapshot = db.collection("pets").get().await()
            val pets = snapshot.toObjects(Pet::class.java)
            Result.success(pets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
