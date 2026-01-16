package com.example.furfriends.ui.profileedit

import com.example.furfriends.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileEditRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // This fetches the current user's data from Firestore.
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

    // This function saves the updated user data back to Firestore.
    suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            val firebaseUser = auth.currentUser ?: throw Exception("No user logged in")
            db.collection("users").document(firebaseUser.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
