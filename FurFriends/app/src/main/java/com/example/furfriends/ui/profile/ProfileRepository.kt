package com.example.furfriends.ui.profile

import com.example.furfriends.data.Pet
import com.example.furfriends.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // This function now fetches the logged-in user's profile from Firestore.
    suspend fun getUserProfile(): Result<User> {
        return try {
            val firebaseUser = auth.currentUser ?: throw Exception("No user logged in")
            val document = db.collection("users").document(firebaseUser.uid).get().await()
            val user = document.toObject(User::class.java) ?: throw Exception("User data not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // This function now fetches the pets owned by the current user from Firestore.
    suspend fun getMyPets(): Result<List<Pet>> {
        return try {
            val firebaseUser = auth.currentUser ?: throw Exception("No user logged in")
            val snapshot = db.collection("pets").whereEqualTo("ownerId", firebaseUser.uid).get().await()
            val pets = snapshot.toObjects(Pet::class.java)
            Result.success(pets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
