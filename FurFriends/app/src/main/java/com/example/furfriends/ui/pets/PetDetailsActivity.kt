package com.example.furfriends.ui.pets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import coil.load
import com.example.furfriends.AdoptionConfirmationActivity
import com.example.furfriends.R
import com.example.furfriends.ui.petdetails.PetDetailsViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PetDetailsActivity : AppCompatActivity() {

    private val viewModel: PetDetailsViewModel by viewModels()
    private var ownerPhone: String = ""
    private var requestStatus: String? = null

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
        observeOwnerDetails()
        observeRequestStatus()
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
            if (!requestStatus.isNullOrBlank()) {
                Toast.makeText(this, "You already requested adoption", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, AdoptionConfirmationActivity::class.java)
            intent.putExtra("petId", petId)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_call_owner).setOnClickListener {
            openDialer(ownerPhone)
        }

        findViewById<Button>(R.id.btn_whatsapp_owner).setOnClickListener {
            openWhatsApp(ownerPhone)
        }
    }

    private fun observePetDetails() {
        val petImageView: ImageView = findViewById(R.id.iv_pet_detail_image)
        viewModel.petDetails.observe(this) { pet ->
            findViewById<TextView>(R.id.tv_pet_detail_name).text = pet.name
            findViewById<Chip>(R.id.chip_age).text = pet.age
            findViewById<Chip>(R.id.chip_breed).text = pet.breed
            findViewById<Chip>(R.id.chip_weight).text = pet.weight
            findViewById<TextView>(R.id.tv_pet_detail_location).text = pet.locationName
            findViewById<TextView>(R.id.tv_pet_detail_about).text = pet.story

            petImageView.load(pet.imageUrls.firstOrNull()) {
                placeholder(R.drawable.logo)
                error(R.drawable.logo)
            }
        }
    }

    private fun observeOwnerDetails() {
        viewModel.ownerDetails.observe(this) { owner ->
            ownerPhone = owner.phone
        }
    }

    private fun observeRequestStatus() {
        viewModel.requestStatus.observe(this) { status ->
            requestStatus = status
            val statusView = findViewById<TextView>(R.id.tv_adoption_status)
            val adoptFab = findViewById<FloatingActionButton>(R.id.fab_adopt)
            if (status.isNullOrBlank()) {
                statusView.visibility = android.view.View.GONE
                adoptFab.isEnabled = true
                adoptFab.alpha = 1.0f
            } else {
                statusView.visibility = android.view.View.VISIBLE
                statusView.text = "Status: $status"
                adoptFab.isEnabled = false
                adoptFab.alpha = 0.5f
            }
        }
    }

    private fun openDialer(phone: String) {
        if (phone.isBlank()) {
            Toast.makeText(this, "Owner phone not available", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        startActivity(intent)
    }

    private fun openWhatsApp(phone: String) {
        if (phone.isBlank()) {
            Toast.makeText(this, "Owner phone not available", Toast.LENGTH_SHORT).show()
            return
        }
        val formatted = formatBangladeshPhone(phone)
        val uri = Uri.parse("https://wa.me/$formatted")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun formatBangladeshPhone(raw: String): String {
        val digits = raw.filter { it.isDigit() }
        return when {
            digits.startsWith("880") -> digits
            digits.startsWith("0") -> "880" + digits.drop(1)
            else -> digits
        }
    }
}

