package com.example.furfriends.ui.location

import com.example.furfriends.data.Location
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LocationRepository {

    private val db = FirebaseFirestore.getInstance()

    // This function now fetches the list of locations from your Cloud Firestore database.
    suspend fun getLocations(): Result<List<Location>> {
        return try {
            val snapshot = db.collection("locations").get().await()
            val locations = snapshot.toObjects(Location::class.java)
            Result.success(locations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
