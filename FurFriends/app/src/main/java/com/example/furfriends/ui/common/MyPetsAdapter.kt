package com.example.furfriends.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.furfriends.R
import com.example.furfriends.data.Pet
import java.io.File

class MyPetsAdapter(private var pets: List<Pet>) : RecyclerView.Adapter<MyPetsAdapter.MyPetViewHolder>() {

    var onItemClick: ((Pet) -> Unit)? = null
    var onEditClick: ((Pet) -> Unit)? = null
    var onDeleteClick: ((Pet) -> Unit)? = null
    var onMarkAdoptedClick: ((Pet) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_pet, parent, false)
        return MyPetViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyPetViewHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet)
        holder.itemView.setOnClickListener { onItemClick?.invoke(pet) }
        holder.editButton.setOnClickListener { onEditClick?.invoke(pet) }
        holder.deleteButton.setOnClickListener { onDeleteClick?.invoke(pet) }
        holder.adoptedButton.setOnClickListener { onMarkAdoptedClick?.invoke(pet) }
    }

    override fun getItemCount() = pets.size

    fun updatePets(newPets: List<Pet>) {
        pets = newPets
        notifyDataSetChanged()
    }

    class MyPetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_my_pet_name)
        private val statusPill: TextView = itemView.findViewById(R.id.tv_my_pet_status_pill)
        private val ageTextView: TextView = itemView.findViewById(R.id.tv_my_pet_age)
        private val breedTextView: TextView = itemView.findViewById(R.id.tv_my_pet_breed)
        private val weightTextView: TextView = itemView.findViewById(R.id.tv_my_pet_weight)
        private val locationTextView: TextView = itemView.findViewById(R.id.tv_my_pet_location)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_my_pet_image)
        val editButton: MaterialButton = itemView.findViewById(R.id.btn_my_pet_edit)
        val adoptedButton: MaterialButton = itemView.findViewById(R.id.btn_my_pet_adopted)
        val deleteButton: MaterialButton = itemView.findViewById(R.id.btn_my_pet_delete)

        fun bind(pet: Pet) {
            nameTextView.text = pet.name
            val statusText = pet.status.replaceFirstChar { it.uppercaseChar() }
            statusPill.text = statusText
            applyChip(ageTextView, pet.age)
            applyChip(breedTextView, pet.breed)
            applyChip(weightTextView, pet.weight)
            locationTextView.text = pet.locationName

            if (pet.status.equals("adopted", ignoreCase = true)) {
                adoptedButton.isEnabled = false
                adoptedButton.alpha = 0.5f
            } else {
                adoptedButton.isEnabled = true
                adoptedButton.alpha = 1.0f
            }

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

