package com.example.furfriends.ui.pets

import com.example.furfriends.data.Pet
import com.example.furfriends.data.PetMapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PetEditRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getPet(petId: String): Result<Pet> {
        return try {
            val document = db.collection("pets").document(petId).get().await()
            val pet = PetMapper.fromDocument(document)
            Result.success(pet)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePet(petId: String, story: String, status: String): Result<Unit> {
        return try {
            db.collection("pets")
                .document(petId)
                .update(mapOf("story" to story, "status" to status))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
