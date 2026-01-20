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

class PetEditActivity : AppCompatActivity() {

    private val viewModel: PetEditViewModel by viewModels()
    private lateinit var etStatus: AutoCompleteTextView
    private lateinit var etStory: EditText
    private lateinit var tvName: TextView
    private var petId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_edit)

        petId = intent.getStringExtra("petId")
        if (petId == null) {
            Toast.makeText(this, "Pet not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        etStatus = findViewById(R.id.et_edit_pet_status)
        etStory = findViewById(R.id.et_edit_pet_story)
        tvName = findViewById(R.id.tv_edit_pet_name)

        val statuses = listOf("available", "adopted")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, statuses)
        etStatus.setAdapter(adapter)

        findViewById<ImageView>(R.id.iv_back_arrow_edit_pet).setOnClickListener { finish() }
        findViewById<TextView>(R.id.btn_save_pet_changes).setOnClickListener { saveChanges() }

        viewModel.pet.observe(this) { pet ->
            tvName.text = pet.name
            etStatus.setText(pet.status, false)
            etStory.setText(pet.story)
        }

        viewModel.updateStatus.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Post updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            result.onFailure {
                Toast.makeText(this, "Update failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadPet(petId!!)
    }

    private fun saveChanges() {
        val status = etStatus.text.toString().trim()
        val story = etStory.text.toString().trim()
        if (status.isEmpty() || story.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.saveChanges(petId!!, story, status)
    }
}
