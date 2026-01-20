package com.example.furfriends.ui.pets

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.R
import com.example.furfriends.data.Pet
import com.example.furfriends.data.SeedLocations
import com.example.furfriends.ui.uploadpet.UploadPetViewModel
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream

class UploadPetActivity : AppCompatActivity() {

    private val viewModel: UploadPetViewModel by viewModels()

    // UI Elements
    private lateinit var etName: EditText
    private lateinit var etBreed: EditText
    private lateinit var etType: AutoCompleteTextView
    private lateinit var etAge: EditText
    private lateinit var etWeight: EditText
    private lateinit var etLocation: AutoCompleteTextView
    private lateinit var etStory: EditText
    private lateinit var petImageView: ImageView
    private lateinit var previewAdapter: UploadImagePreviewAdapter
    private val selectedImagePaths = mutableListOf<String>()

    private val imagePicker = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        if (uris.isNullOrEmpty()) return@registerForActivityResult
        val limited = uris.take(5)
        if (uris.size > 5) {
            Toast.makeText(this, "You can add up to 5 images", Toast.LENGTH_SHORT).show()
        }
        val paths = limited.mapNotNull { copyImageToInternal(it) }
        if (paths.isEmpty()) {
            Toast.makeText(this, "Failed to load images", Toast.LENGTH_SHORT).show()
            return@registerForActivityResult
        }
        selectedImagePaths.clear()
        selectedImagePaths.addAll(paths)
        previewAdapter.submitImages(selectedImagePaths)
        val first = selectedImagePaths.firstOrNull()
        if (first != null) {
            petImageView.setImageURI(android.net.Uri.fromFile(File(first)))
        }
    }

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
        val previewRecycler: RecyclerView = findViewById(R.id.rv_pet_images_upload)
        etName = findViewById(R.id.et_pet_name_upload)
        etBreed = findViewById(R.id.et_pet_breed_upload)
        etType = findViewById(R.id.et_pet_type_upload)
        etAge = findViewById(R.id.et_pet_age_upload)
        etWeight = findViewById(R.id.et_pet_weight_upload)
        etLocation = findViewById(R.id.et_pet_location_upload)
        etStory = findViewById(R.id.et_pet_story_upload)

        val locationNames = SeedLocations.dhakaAreas.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationNames)
        etLocation.setAdapter(adapter)

        val types = listOf("Dog", "Cat", "Other")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, types)
        etType.setAdapter(typeAdapter)

        previewAdapter = UploadImagePreviewAdapter(emptyList())
        previewRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        previewRecycler.adapter = previewAdapter
    }

    private fun setupListeners() {
        val backArrow: ImageView = findViewById(R.id.iv_back_arrow_upload)
        backArrow.setOnClickListener {
            finish()
        }

        petImageView.setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
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
        val type = etType.text.toString().trim()
        val age = etAge.text.toString().trim()
        val weight = etWeight.text.toString().trim()
        val locationName = etLocation.text.toString().trim()
        val story = etStory.text.toString().trim()

        // Basic validation
        if (name.isEmpty() || breed.isEmpty() || type.isEmpty() || age.isEmpty() || weight.isEmpty() || locationName.isEmpty() || story.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImagePaths.isEmpty()) {
            Toast.makeText(this, "Please select pet photos", Toast.LENGTH_SHORT).show()
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
            type = type,
            weight = weight,
            story = story,
            locationId = selectedLocation.id,
            locationName = selectedLocation.name,
            ownerId = ownerId,
            status = "available",
            imageUrls = selectedImagePaths.toList()
        )

        viewModel.uploadPet(pet)
    }

    private fun copyImageToInternal(uri: Uri): String? {
        return try {
            val input = contentResolver.openInputStream(uri) ?: return null
            val file = File(filesDir, "pet_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
            input.close()
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}

