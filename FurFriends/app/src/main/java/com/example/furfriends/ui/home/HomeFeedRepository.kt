package com.example.furfriends.ui.home

import com.example.furfriends.data.Pet
import com.example.furfriends.data.PetMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class HomeFeedRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun getFeedPets(): Result<List<Pet>> {
        return try {
            val snapshot = db.collection("pets")
                .whereEqualTo("status", "available")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val pets = PetMapper.fromQuery(snapshot)
            Result.success(pets)
        } catch (e: Exception) {
            try {
                val fallbackSnapshot = db.collection("pets")
                    .whereEqualTo("status", "available")
                    .get()
                    .await()
                val pets = PetMapper.fromQuery(fallbackSnapshot)
                Result.success(pets)
            } catch (fallback: Exception) {
                Result.failure(fallback)
            }
        }
    }

    suspend fun getMyRequestStatuses(): Result<Map<String, String>> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("No user logged in")
            val snapshot = db.collectionGroup("requests")
                .whereEqualTo("requesterId", uid)
                .get()
                .await()
            val map = snapshot.documents.mapNotNull { doc ->
                val petId = doc.getString("petId")
                val status = doc.getString("status")
                if (petId.isNullOrBlank() || status.isNullOrBlank()) null else petId to status
            }.toMap()
            Result.success(map)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
