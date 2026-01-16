package com.example.furfriends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.furfriends.data.Location // Correct data class is already imported

class LocationAdapter(private var locations: List<Location>) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    var onItemClick: ((Location) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(location)
        }
    }

    override fun getItemCount() = locations.size

    fun updateLocations(newLocations: List<Location>) {
        locations = newLocations
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_location_name)
        private val addressTextView: TextView = itemView.findViewById(R.id.tv_location_distance) // This TextView will now hold the address
        private val imageView: ImageView = itemView.findViewById(R.id.iv_location_image)

        fun bind(location: Location) {
            nameTextView.text = location.name
            addressTextView.text = location.address // Now setting the full address

            // Load the image from the URL using Coil
            imageView.load(location.imageUrl) {
                placeholder(R.drawable.logo) // Show logo as a placeholder
                error(R.drawable.logo) // Show logo if there is an error
            }
        }
    }
}
