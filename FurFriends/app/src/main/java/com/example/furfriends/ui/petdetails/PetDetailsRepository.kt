package com.example.furfriends.ui.petdetails

import com.example.furfriends.data.Pet
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
}
