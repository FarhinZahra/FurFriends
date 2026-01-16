package com.example.furfriends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.furfriends.data.Pet

class MyPetsAdapter(private var pets: List<Pet>) : RecyclerView.Adapter<MyPetsAdapter.MyPetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_pet, parent, false)
        return MyPetViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyPetViewHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet)
    }

    override fun getItemCount() = pets.size

    fun updatePets(newPets: List<Pet>) {
        pets = newPets
        notifyDataSetChanged()
    }

    class MyPetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_my_pet_name)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_my_pet_image)

        fun bind(pet: Pet) {
            nameTextView.text = pet.name

            // Load the image from the URL using Coil
            imageView.load(pet.imageUrl) {
                placeholder(R.drawable.logo)
                error(R.drawable.logo)
            }
        }
    }
}
