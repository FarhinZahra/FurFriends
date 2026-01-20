package com.example.furfriends.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.furfriends.R
import com.example.furfriends.data.Pet
import java.io.File

class FavoritesAdapter(private var pets: List<Pet>) : RecyclerView.Adapter<FavoritesAdapter.PetViewHolder>() {

    var onItemClick: ((Pet) -> Unit)? = null
    var onFavoriteToggle: ((Pet) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(pet)
        }
        holder.favoriteIcon.setOnClickListener {
            onFavoriteToggle?.invoke(pet)
        }
    }

    override fun getItemCount() = pets.size

    fun updatePets(newPets: List<Pet>) {
        pets = newPets
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_pet_name)
        private val ageChip: TextView = itemView.findViewById(R.id.tv_favorite_chip_age)
        private val breedChip: TextView = itemView.findViewById(R.id.tv_favorite_chip_breed)
        private val weightChip: TextView = itemView.findViewById(R.id.tv_favorite_chip_weight)
        private val statusTextView: TextView = itemView.findViewById(R.id.tv_favorite_status)
        private val locationTextView: TextView = itemView.findViewById(R.id.tv_favorite_location)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_pet_image)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.iv_favorite_icon)

        fun bind(pet: Pet) {
            nameTextView.text = pet.name
            statusTextView.text = pet.status.replaceFirstChar { it.uppercaseChar() }
            locationTextView.text = pet.locationName
            applyChip(ageChip, pet.age)
            applyChip(breedChip, pet.breed)
            applyChip(weightChip, pet.weight)

            // Load the image from the URL using Coil
            val image = pet.imageUrls.firstOrNull()
            val data = if (image != null && image.startsWith("/")) File(image) else image
            imageView.load(data) {
                placeholder(R.drawable.logo)
                error(R.drawable.logo)
            }
        }

        private fun applyChip(view: TextView, value: String) {
            if (value.isBlank()) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
                view.text = value
            }
        }
    }
}

