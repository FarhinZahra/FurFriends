package com.example.furfriends

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import coil.load
import com.example.furfriends.ui.chat.ChatMessageActivity
import com.example.furfriends.ui.petdetails.PetDetailsViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PetDetailsActivity : AppCompatActivity() {

    private val viewModel: PetDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)

        val petId = intent.getStringExtra("petId")
        if (petId == null) {
            Toast.makeText(this, "Pet ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // --- Setup ---
        setupToolbar()
        observePetDetails()
        setupClickListeners(petId)

        // Load initial data
        viewModel.loadPetDetails(petId)
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupClickListeners(petId: String) {
        findViewById<FloatingActionButton>(R.id.fab_adopt).setOnClickListener {
            val intent = Intent(this, AdoptionConfirmationActivity::class.java)
            intent.putExtra("petId", petId)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_chat_with_owner).setOnClickListener {
            val pet = viewModel.petDetails.value
            if (pet != null) {
                val intent = Intent(this, ChatMessageActivity::class.java)
                // We need to create a unique conversation ID. A simple way is to combine user IDs.
                // This logic should be improved in a real app.
                val conversationId = createConversationId(pet.ownerId)
                intent.putExtra("conversationId", conversationId)
                intent.putExtra("chatParticipantName", "Owner") // We can pass the owner's name here later
                startActivity(intent)
            } else {
                Toast.makeText(this, "Pet details not loaded yet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observePetDetails() {
        val petImageView: ImageView = findViewById(R.id.iv_pet_detail_image)
        viewModel.petDetails.observe(this) { pet ->
            val details = pet.details.split(",")
            findViewById<TextView>(R.id.tv_pet_detail_name).text = pet.name
            findViewById<Chip>(R.id.chip_age).text = details.getOrNull(0)?.trim() ?: ""
            findViewById<Chip>(R.id.chip_breed).text = details.getOrNull(1)?.trim() ?: ""
            findViewById<Chip>(R.id.chip_weight).text = details.getOrNull(2)?.trim() ?: ""
            findViewById<TextView>(R.id.tv_pet_detail_location).text = pet.location
            findViewById<TextView>(R.id.tv_pet_detail_about).text = pet.story

            petImageView.load(pet.imageUrl) {
                placeholder(R.drawable.logo)
                error(R.drawable.logo)
            }
        }
    }

    private fun createConversationId(ownerId: String): String {
        val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
        return if (currentUserId < ownerId) {
            "${currentUserId}_${ownerId}"
        } else {
            "${ownerId}_${currentUserId}"
        }
    }
}
