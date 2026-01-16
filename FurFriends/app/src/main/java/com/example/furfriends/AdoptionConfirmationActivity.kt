package com.example.furfriends

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.furfriends.ui.adoptionconfirmation.AdoptionConfirmationViewModel

class AdoptionConfirmationActivity : AppCompatActivity() {

    private val viewModel: AdoptionConfirmationViewModel by viewModels()
    private var petName: String = ""
    private var petOwnerId: String = ""
    private var requesterName: String = ""
    private var requesterPhone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adoption_confirmation)

        // Get the petId from the intent
        val petId = intent.getStringExtra("petId")
        if (petId == null) {
            Toast.makeText(this, "Pet ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // --- Observers ---
        observePetDetails()
        observeUserDetails()
        observeAdoptionStatus()

        // --- Click Listeners ---
        findViewById<ImageView>(R.id.iv_back_arrow_confirm).setOnClickListener {
            finish() // Close the activity and go back
        }

        findViewById<Button>(R.id.btn_confirm_adoption).setOnClickListener {
            viewModel.submitAdoptionRequest(
                petId = petId,
                petName = petName,
                ownerId = petOwnerId,
                requesterName = requesterName,
                requesterPhone = requesterPhone
            )
        }

        // Tell the ViewModel to load the data
        viewModel.loadConfirmationData(petId)
    }

    private fun observePetDetails() {
        viewModel.petDetails.observe(this) { pet ->
            findViewById<TextView>(R.id.tv_pet_confirm_name).text = pet.name
            findViewById<TextView>(R.id.tv_pet_confirm_details).text = pet.displayDetails()
            petName = pet.name
            petOwnerId = pet.ownerId
            // We would also load the pet's image here using Glide or Picasso
        }
    }

    private fun observeUserDetails() {
        viewModel.userDetails.observe(this) { user ->
            findViewById<TextView>(R.id.tv_adopter_name).text = user.name
            findViewById<TextView>(R.id.tv_adopter_contact).text = user.phone
            requesterName = user.name
            requesterPhone = user.phone
        }
    }

    private fun observeAdoptionStatus() {
        viewModel.adoptionStatus.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Adoption request sent!", Toast.LENGTH_LONG).show()
                finish()
            }
            result.onFailure {
                Toast.makeText(this, "Request failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
