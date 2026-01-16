package com.example.furfriends

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.furfriends.data.Pet
import com.example.furfriends.ui.uploadpet.UploadPetViewModel

class UploadPetActivity : AppCompatActivity() {

    private val viewModel: UploadPetViewModel by viewModels()

    // UI Elements
    private lateinit var etName: EditText
    private lateinit var etBreed: EditText
    private lateinit var etAge: EditText
    private lateinit var etWeight: EditText
    private lateinit var etLocation: EditText
    private lateinit var etStory: EditText

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
        etName = findViewById(R.id.et_pet_name_upload)
        etBreed = findViewById(R.id.et_pet_breed_upload)
        etAge = findViewById(R.id.et_pet_age_upload)
        etWeight = findViewById(R.id.et_pet_weight_upload)
        etLocation = findViewById(R.id.et_pet_location_upload)
        etStory = findViewById(R.id.et_pet_story_upload)
    }

    private fun setupListeners() {
        val backArrow: ImageView = findViewById(R.id.iv_back_arrow_upload)
        backArrow.setOnClickListener {
            finish()
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
        val location = etLocation.text.toString().trim()
        val story = etStory.text.toString().trim()

        // Basic validation
        if (name.isEmpty() || breed.isEmpty() || age.isEmpty() || weight.isEmpty() || location.isEmpty() || story.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // For now, we'll create the details string manually. We can improve this later.
        val details = "$age, $breed, $weight"

        val pet = Pet(
            name = name,
            details = details,
            imageUrl = "" // We would handle image upload separately
        )

        viewModel.uploadPet(pet)
    }
}
