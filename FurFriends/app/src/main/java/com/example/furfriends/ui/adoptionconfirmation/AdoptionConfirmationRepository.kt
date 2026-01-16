package com.example.furfriends.ui.adoptionconfirmation

import com.example.furfriends.data.Pet
import com.example.furfriends.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdoptionConfirmationRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Fetches the specific pet's details from Firestore.
    suspend fun getPetDetails(petId: String): Result<Pet> {
        return try {
            val document = db.collection("pets").document(petId).get().await()
            val pet = document.toObject(Pet::class.java) ?: throw Exception("Pet not found")
            Result.success(pet)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fetches the current user's details from Firestore.
    suspend fun getUserDetails(): Result<User> {
        return try {
            val firebaseUser = auth.currentUser ?: throw Exception("No user logged in")
            val document = db.collection("users").document(firebaseUser.uid).get().await()
            val user = document.toObject(User::class.java) ?: throw Exception("User data not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
