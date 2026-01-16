package com.example.furfriends.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.furfriends.R
import com.example.furfriends.data.Pet

class HomeFeedAdapter(private var pets: List<Pet>) : RecyclerView.Adapter<HomeFeedAdapter.PostViewHolder>() {

    var onItemClick: ((Pet) -> Unit)? = null
    var onFavoriteToggle: ((Pet) -> Unit)? = null
    private var favoriteIds: Set<String> = emptySet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet, favoriteIds.contains(pet.id))
        holder.itemView.setOnClickListener { onItemClick?.invoke(pet) }
        holder.favoriteIcon.setOnClickListener { onFavoriteToggle?.invoke(pet) }
        holder.viewButton.setOnClickListener { onItemClick?.invoke(pet) }
    }

    override fun getItemCount() = pets.size

    fun updatePets(newPets: List<Pet>) {
        pets = newPets
        notifyDataSetChanged()
    }

    fun setFavoriteIds(ids: Set<String>) {
        favoriteIds = ids
        notifyDataSetChanged()
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_post_name)
        private val ageChip: TextView = itemView.findViewById(R.id.tv_chip_age)
        private val breedChip: TextView = itemView.findViewById(R.id.tv_chip_breed)
        private val weightChip: TextView = itemView.findViewById(R.id.tv_chip_weight)
        private val locationTextView: TextView = itemView.findViewById(R.id.tv_post_location)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_post_image)
        private val statusView: TextView = itemView.findViewById(R.id.tv_post_status)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.iv_post_favorite)
        val viewButton: Button = itemView.findViewById(R.id.btn_post_view)

        fun bind(pet: Pet, isFavorite: Boolean) {
            nameTextView.text = pet.name
            applyChip(ageChip, pet.age)
            applyChip(breedChip, pet.breed)
            applyChip(weightChip, pet.weight)
            locationTextView.text = pet.locationName
            statusView.text = pet.status.replaceFirstChar { it.uppercaseChar() }
            favoriteIcon.setImageResource(
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_outline
            )
            imageView.load(pet.imageUrls.firstOrNull()) {
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
