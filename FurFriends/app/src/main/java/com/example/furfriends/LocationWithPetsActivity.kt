package com.example.furfriends

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.ui.locationdetails.LocationDetailsViewModel
import com.google.android.material.chip.Chip

class LocationWithPetsActivity : AppCompatActivity() {

    private val viewModel: LocationDetailsViewModel by viewModels()
    private lateinit var petsAdapter: FavoritesAdapter // We can reuse the FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_with_pets)

        // Get the locationId from the intent
        val locationId = intent.getStringExtra("locationId")
        if (locationId == null) {
            Toast.makeText(this, "Location ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // --- Pet List Setup ---
        val petsRecyclerView: RecyclerView = findViewById(R.id.rv_pets_in_location)
        petsRecyclerView.layoutManager = GridLayoutManager(this, 2)

        // Initialize the adapter with an empty list
        petsAdapter = FavoritesAdapter(emptyList())
        petsRecyclerView.adapter = petsAdapter

        // --- Observers ---
        observeLocationDetails()
        observePetsList()

        // --- Click Listeners ---
        findViewById<ImageView>(R.id.iv_back_arrow_location).setOnClickListener {
            finish() // Go back to the previous screen
        }

        findViewById<Chip>(R.id.chip_dog).setOnClickListener {
            // Handle dog filter logic here (can be moved to ViewModel)
        }

        findViewById<Chip>(R.id.chip_cat).setOnClickListener {
            // Handle cat filter logic here (can be moved to ViewModel)
        }

        // Set the click listener for the adapter
        petsAdapter.onItemClick = {
            val intent = Intent(this, PetDetailsActivity::class.java)
            // Later, we can pass the pet ID to the next screen
            intent.putExtra("petId", it.id)
            startActivity(intent)
        }

        // Tell the ViewModel to load the data for the specific location
        viewModel.loadLocationDetails(locationId)
    }

    private fun observeLocationDetails() {
        val title: TextView = findViewById(R.id.tv_location_title)
        val address: TextView = findViewById(R.id.tv_location_address)

        viewModel.locationDetails.observe(this) { location ->
            title.text = location.name
            address.text = location.address // Now correctly using the address field
        }
    }

    private fun observePetsList() {
        viewModel.pets.observe(this) { pets ->
            petsAdapter.updatePets(pets)
        }
    }
}
