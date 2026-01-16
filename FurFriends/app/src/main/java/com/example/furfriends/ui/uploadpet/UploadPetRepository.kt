package com.example.furfriends.ui.uploadpet

import com.example.furfriends.data.Pet
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

class UploadPetRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun uploadPet(pet: Pet): Result<Unit> {
        return try {
            // The "pets" collection will be created automatically by Firestore
            val data = hashMapOf<String, Any>(
                "name" to pet.name,
                "age" to pet.age,
                "breed" to pet.breed,
                "weight" to pet.weight,
                "story" to pet.story,
                "imageUrls" to pet.imageUrls,
                "locationId" to pet.locationId,
                "locationName" to pet.locationName,
                "ownerId" to pet.ownerId,
                "status" to pet.status,
                "createdAt" to FieldValue.serverTimestamp()
            )
            db.collection("pets").add(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
