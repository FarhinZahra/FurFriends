package com.example.furfriends.ui.locationdetails

import com.example.furfriends.data.Location
import com.example.furfriends.data.Pet
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LocationDetailsRepository {

    private val db = FirebaseFirestore.getInstance()

    // This function now fetches a specific location document from Firestore.
    suspend fun getLocationDetails(locationId: String): Result<Location> {
        return try {
            val document = db.collection("locations").document(locationId).get().await()
            val location = document.toObject(Location::class.java) ?: throw Exception("Location not found")
            Result.success(location)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // This function now fetches only the pets for a specific location.
    suspend fun getPetsForLocation(locationId: String): Result<List<Pet>> {
        return try {
            val snapshot = db.collection("pets").whereEqualTo("locationId", locationId).get().await()
            val pets = snapshot.toObjects(Pet::class.java)
            Result.success(pets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
