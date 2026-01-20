package com.example.furfriends.ui.profile

import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.furfriends.R
import com.example.furfriends.data.User
import com.example.furfriends.ui.profileedit.ProfileEditViewModel
import com.google.android.material.button.MaterialButton
import java.io.File
import java.io.FileOutputStream

class ProfileEditActivity : AppCompatActivity() {

    private val viewModel: ProfileEditViewModel by viewModels()

    // UI Elements
    private lateinit var etUsername: EditText
    private lateinit var etContact: EditText
    private lateinit var etEmail: EditText
    private lateinit var etLocation: EditText
    private lateinit var profileImageView: ImageView
    private var selectedPhotoPath: String? = null

    private val imagePicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@registerForActivityResult
        val path = copyImageToInternal(uri)
        if (path == null) {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            return@registerForActivityResult
        }
        selectedPhotoPath = path
        profileImageView.load(File(path)) {
            placeholder(R.drawable.img)
            error(R.drawable.img)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        // Initialize UI Elements
        initializeViews()

        // Set up listeners
        setupListeners()

        // Observe LiveData from the ViewModel
        observeUserProfile()
        observeUpdateStatus()

        // Tell the ViewModel to load the initial data
        viewModel.loadUserProfile()
    }

    private fun initializeViews() {
        etUsername = findViewById(R.id.et_username_edit)
        etContact = findViewById(R.id.et_contact_edit)
        etEmail = findViewById(R.id.et_email_edit)
        etLocation = findViewById(R.id.et_location_edit)
        profileImageView = findViewById(R.id.iv_profile_picture_edit)
    }

    private fun setupListeners() {
        val backArrow: ImageView = findViewById(R.id.iv_back_arrow_edit)
        backArrow.setOnClickListener {
            finish() // Close the activity and go back
        }

        val saveButton: MaterialButton = findViewById(R.id.tv_save_button)
        saveButton.setOnClickListener {
            saveProfile()
        }

        profileImageView.setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
        }
        findViewById<android.widget.TextView>(R.id.tv_change_photo).setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
        }
    }

    private fun observeUserProfile() {
        viewModel.userProfile.observe(this) { user ->
            etUsername.setText(user.name) // Use name for username field
            etContact.setText(user.phone)
            etEmail.setText(user.email)
            etLocation.setText(user.address)
            selectedPhotoPath = user.photoUrl.ifBlank { null }
            val photo = user.photoUrl
            val data = if (photo.startsWith("/")) File(photo) else photo
            profileImageView.load(data) {
                placeholder(R.drawable.img)
                error(R.drawable.img)
            }
        }
    }

    private fun observeUpdateStatus() {
        viewModel.updateStatus.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
                finish() // Go back to the profile screen
            }
            result.onFailure {
                Toast.makeText(this, "Failed to save profile: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveProfile() {
        val updatedUser = User(
            // We need the original UID and email, which we get from the observed user
            uid = viewModel.userProfile.value?.uid ?: "",
            email = viewModel.userProfile.value?.email ?: "",
            name = etUsername.text.toString().trim(),
            phone = etContact.text.toString().trim(),
            address = etLocation.text.toString().trim(),
            photoUrl = selectedPhotoPath ?: viewModel.userProfile.value?.photoUrl.orEmpty()
            // We would also save Bio and BirthDate here
        )

        viewModel.updateUserProfile(updatedUser)
    }

    private fun copyImageToInternal(uri: Uri): String? {
        return try {
            val input = contentResolver.openInputStream(uri) ?: return null
            val file = File(filesDir, "profile_${System.currentTimeMillis()}.jpg")
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

