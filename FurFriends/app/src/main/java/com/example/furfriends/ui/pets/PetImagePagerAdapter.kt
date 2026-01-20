package com.example.furfriends.ui.pets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.furfriends.R
import java.io.File

class PetImagePagerAdapter(
    private var images: List<String>
) : RecyclerView.Adapter<PetImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet_image_pager, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    fun submitImages(newImages: List<String>) {
        images = newImages
        notifyDataSetChanged()
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_pet_image)

        fun bind(path: String) {
            if (path.isBlank()) {
                imageView.load(R.drawable.logo)
                return
            }
            val data = if (path.startsWith("/")) File(path) else path
            imageView.load(data) {
                placeholder(R.drawable.logo)
                error(R.drawable.logo)
            }
        }
    }
}
