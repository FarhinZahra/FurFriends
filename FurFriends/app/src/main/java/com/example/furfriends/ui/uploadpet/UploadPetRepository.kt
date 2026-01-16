package com.example.furfriends.ui.uploadpet

import com.example.furfriends.data.Pet
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UploadPetRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun uploadPet(pet: Pet): Result<Unit> {
        return try {
            // The "pets" collection will be created automatically by Firestore
            db.collection("pets").add(pet).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
