package com.example.furfriends.ui.home

import com.example.furfriends.data.Pet
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class HomeFeedRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getFeedPets(): Result<List<Pet>> {
        return try {
            val snapshot = db.collection("pets")
                .whereEqualTo("status", "available")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val pets = snapshot.toObjects(Pet::class.java)
            Result.success(pets)
        } catch (e: Exception) {
            try {
                val fallbackSnapshot = db.collection("pets")
                    .whereEqualTo("status", "available")
                    .get()
                    .await()
                val pets = fallbackSnapshot.toObjects(Pet::class.java)
                Result.success(pets)
            } catch (fallback: Exception) {
                Result.failure(fallback)
            }
        }
    }
}
