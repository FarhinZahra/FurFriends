package com.example.furfriends

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.furfriends.data.User
import com.example.furfriends.ui.profileedit.ProfileEditViewModel

class ProfileEditActivity : AppCompatActivity() {

    private val viewModel: ProfileEditViewModel by viewModels()

    // UI Elements
    private lateinit var etUsername: EditText
    private lateinit var etContact: EditText
    private lateinit var etEmail: EditText
    private lateinit var etBio: EditText
    private lateinit var etLocation: EditText
    // We'll treat birth date as a simple string for now
    private lateinit var etBirthDate: EditText

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
        etBio = findViewById(R.id.et_bio_edit)
        etLocation = findViewById(R.id.et_location_edit)
        etBirthDate = findViewById(R.id.et_birthdate_edit)
    }

    private fun setupListeners() {
        val backArrow: ImageView = findViewById(R.id.iv_back_arrow_edit)
        backArrow.setOnClickListener {
            finish() // Close the activity and go back
        }

        val saveButton: TextView = findViewById(R.id.tv_save_button)
        saveButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun observeUserProfile() {
        viewModel.userProfile.observe(this) { user ->
            etUsername.setText(user.name) // Use name for username field
            etContact.setText(user.phone)
            etEmail.setText(user.email)
            etLocation.setText(user.address)
            // Bio and BirthDate would be new fields in your User model
            // For now, we leave them blank or with placeholder text
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
            address = etLocation.text.toString().trim()
            // We would also save Bio and BirthDate here
        )

        viewModel.updateUserProfile(updatedUser)
    }
}
