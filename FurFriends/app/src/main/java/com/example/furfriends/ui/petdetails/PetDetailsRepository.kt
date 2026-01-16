package com.example.furfriends.ui.petdetails

import com.example.furfriends.data.AdoptionRequest
import com.example.furfriends.data.Pet
import com.example.furfriends.data.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PetDetailsRepository {

    private val db = FirebaseFirestore.getInstance()

    // This function now fetches a specific pet document from Firestore.
    suspend fun getPetDetails(petId: String): Result<Pet> {
        return try {
            val document = db.collection("pets").document(petId).get().await()
            val pet = document.toObject(Pet::class.java) ?: throw Exception("Pet not found")
            Result.success(pet)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOwnerDetails(ownerId: String): Result<User> {
        return try {
            val document = db.collection("users").document(ownerId).get().await()
            val user = document.toObject(User::class.java) ?: throw Exception("Owner not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRequestStatus(petId: String, requesterId: String): Result<String?> {
        return try {
            val snapshot = db.collection("pets")
                .document(petId)
                .collection("requests")
                .whereEqualTo("requesterId", requesterId)
                .get()
                .await()
            val request = snapshot.documents.firstOrNull()?.toObject(AdoptionRequest::class.java)
            Result.success(request?.status)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
