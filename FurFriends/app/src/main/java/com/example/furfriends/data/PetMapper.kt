package com.example.furfriends.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

object PetMapper {

    fun fromDocument(doc: DocumentSnapshot): Pet {
        val base = doc.toObject(Pet::class.java) ?: Pet()
        val imageUrls = (doc.get("imageUrls") as? List<*>)?.mapNotNull { it?.toString() } ?: emptyList()
        val fallbackImage = doc.getString("imageUrl") ?: doc.getString("image")
        val resolvedImages = if (imageUrls.isNotEmpty()) imageUrls else fallbackImage?.let { listOf(it) } ?: emptyList()
        return base.copy(id = doc.id, imageUrls = resolvedImages)
    }

    fun fromQuery(snapshot: QuerySnapshot): List<Pet> {
        return snapshot.documents.map { fromDocument(it) }
    }
}
