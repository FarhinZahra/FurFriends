package com.example.furfriends.ui.favorites

import com.example.furfriends.data.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoritesRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun toggleFavorite(petId: String): Result<Unit> {
        return try {
            val firebaseUser = auth.currentUser ?: throw Exception("No user logged in")
            val favoriteRef = db.collection("users")
                .document(firebaseUser.uid)
                .collection("favorites")
                .document(petId)

            val snapshot = favoriteRef.get().await()
            if (snapshot.exists()) {
                favoriteRef.delete().await()
            } else {
                favoriteRef.set(mapOf("petId" to petId)).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // This function fetches only the pets favorited by the current user.
    suspend fun getFavoritePets(): Result<List<Pet>> {
        return try {
            val firebaseUser = auth.currentUser ?: throw Exception("No user logged in")
            val favoritesSnapshot = db.collection("users")
                .document(firebaseUser.uid)
                .collection("favorites")
                .get()
                .await()

            val favoriteIds = favoritesSnapshot.documents.map { it.id }
            if (favoriteIds.isEmpty()) {
                return Result.success(emptyList())
            }

            val pets = mutableListOf<Pet>()
            val chunks = favoriteIds.chunked(10)
            for (chunk in chunks) {
                val petsSnapshot = db.collection("pets")
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()
                pets += petsSnapshot.toObjects(Pet::class.java)
            }
            Result.success(pets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavoriteIds(): Result<Set<String>> {
        return try {
            val firebaseUser = auth.currentUser ?: throw Exception("No user logged in")
            val favoritesSnapshot = db.collection("users")
                .document(firebaseUser.uid)
                .collection("favorites")
                .get()
                .await()
            Result.success(favoritesSnapshot.documents.map { it.id }.toSet())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
