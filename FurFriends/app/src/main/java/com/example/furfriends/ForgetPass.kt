package com.example.furfriends

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.furfriends.ui.forgetpass.ForgetPassViewModel

class ForgetPass : AppCompatActivity() {

    private val viewModel: ForgetPassViewModel by viewModels()

    // UI Elements
    private lateinit var etEmail: EditText
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_pass)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI Elements
        initializeViews()

        // Set up listeners
        setupListeners()

        // Observe the reset status from the ViewModel
        observeResetStatus()
    }

    private fun initializeViews() {
        etEmail = findViewById(R.id.et_email) // Corrected from emailInput
        btnReset = findViewById(R.id.btn_reset_password) // Corrected ID
    }

    private fun setupListeners() {
        btnReset.setOnClickListener {
            sendResetEmail()
        }
    }

    private fun observeResetStatus() {
        viewModel.resetStatus.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_LONG).show()
                // Optionally, navigate back to the login screen
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            result.onFailure {
                Toast.makeText(this, "Failed to send email: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendResetEmail() {
        val email = etEmail.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return
        }

        viewModel.sendPasswordResetEmail(email)
    }
}
