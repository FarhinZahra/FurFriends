package com.example.furfriends.ui.pets

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.furfriends.R
import java.io.File

class UploadImagePreviewAdapter(
    private var images: List<String>
) : RecyclerView.Adapter<UploadImagePreviewAdapter.PreviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_preview, parent, false)
        return PreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    fun submitImages(newImages: List<String>) {
        images = newImages
        notifyDataSetChanged()
    }

    class PreviewViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.iv_preview_image)
        fun bind(path: String) {
            val data = if (path.startsWith("/")) File(path) else path
            imageView.load(data) {
                placeholder(R.drawable.ic_pets)
                error(R.drawable.ic_pets)
            }
        }
    }
}
