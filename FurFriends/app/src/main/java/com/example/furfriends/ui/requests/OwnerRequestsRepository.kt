package com.example.furfriends.ui.requests

import com.example.furfriends.data.AdoptionRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class OwnerRequestsRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun getRequestsForOwner(): Result<List<AdoptionRequest>> {
        return try {
            val ownerId = auth.currentUser?.uid ?: throw Exception("No user logged in")
            val snapshot = db.collectionGroup("requests")
                .whereEqualTo("ownerId", ownerId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val requests = snapshot.toObjects(AdoptionRequest::class.java)
            Result.success(requests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun approveRequest(request: AdoptionRequest): Result<Unit> {
        return try {
            db.collection("pets")
                .document(request.petId)
                .collection("requests")
                .document(request.id)
                .update("status", "approved")
                .await()

            db.collection("pets")
                .document(request.petId)
                .update("status", "adopted")
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun rejectRequest(request: AdoptionRequest): Result<Unit> {
        return try {
            db.collection("pets")
                .document(request.petId)
                .collection("requests")
                .document(request.id)
                .update("status", "rejected")
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
