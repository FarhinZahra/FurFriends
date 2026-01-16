package com.example.furfriends.ui.pets

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.furfriends.R
import com.example.furfriends.data.Pet
import com.example.furfriends.data.SeedLocations
import com.example.furfriends.ui.uploadpet.UploadPetViewModel
import com.google.firebase.auth.FirebaseAuth

class UploadPetActivity : AppCompatActivity() {

    private val viewModel: UploadPetViewModel by viewModels()

    // UI Elements
    private lateinit var etName: EditText
    private lateinit var etBreed: EditText
    private lateinit var etAge: EditText
    private lateinit var etWeight: EditText
    private lateinit var etLocation: AutoCompleteTextView
    private lateinit var etStory: EditText
    private lateinit var petImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_pet)

        // Initialize UI Elements
        initializeViews()

        // Set up listeners
        setupListeners()

        // Observe the upload status from the ViewModel
        observeUploadStatus()
    }

    private fun initializeViews() {
        petImageView = findViewById(R.id.iv_pet_image_upload)
        etName = findViewById(R.id.et_pet_name_upload)
        etBreed = findViewById(R.id.et_pet_breed_upload)
        etAge = findViewById(R.id.et_pet_age_upload)
        etWeight = findViewById(R.id.et_pet_weight_upload)
        etLocation = findViewById(R.id.et_pet_location_upload)
        etStory = findViewById(R.id.et_pet_story_upload)

        val locationNames = SeedLocations.dhakaAreas.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationNames)
        etLocation.setAdapter(adapter)
    }

    private fun setupListeners() {
        val backArrow: ImageView = findViewById(R.id.iv_back_arrow_upload)
        backArrow.setOnClickListener {
            finish()
        }

        petImageView.setOnClickListener {
            Toast.makeText(this, "Image upload coming soon", Toast.LENGTH_SHORT).show()
        }

        val uploadButton: TextView = findViewById(R.id.tv_upload_button)
        uploadButton.setOnClickListener {
            uploadPet()
        }
    }

    private fun observeUploadStatus() {
        viewModel.uploadStatus.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Pet uploaded successfully!", Toast.LENGTH_LONG).show()
                finish() // Go back to the previous screen
            }
            result.onFailure {
                Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadPet() {
        val name = etName.text.toString().trim()
        val breed = etBreed.text.toString().trim()
        val age = etAge.text.toString().trim()
        val weight = etWeight.text.toString().trim()
        val locationName = etLocation.text.toString().trim()
        val story = etStory.text.toString().trim()

        // Basic validation
        if (name.isEmpty() || breed.isEmpty() || age.isEmpty() || weight.isEmpty() || locationName.isEmpty() || story.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val ownerId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        val selectedLocation = SeedLocations.dhakaAreas.firstOrNull { it.name.equals(locationName, ignoreCase = true) }
        if (selectedLocation == null) {
            etLocation.error = "Select a Dhaka area from the list"
            etLocation.requestFocus()
            return
        }

        val pet = Pet(
            name = name,
            age = age,
            breed = breed,
            weight = weight,
            story = story,
            locationId = selectedLocation.id,
            locationName = selectedLocation.name,
            ownerId = ownerId,
            status = "available",
            imageUrls = emptyList()
        )

        viewModel.uploadPet(pet)
    }
}

