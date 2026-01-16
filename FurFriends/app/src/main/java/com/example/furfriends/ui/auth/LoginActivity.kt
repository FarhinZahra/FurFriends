package com.example.furfriends.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.furfriends.R
import com.example.furfriends.ui.main.MainActivity
import com.example.furfriends.ui.login.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    // UI Elements
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var tvSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI Elements
        initializeViews()

        // Set up listeners
        setupListeners()

        // Observe the login status from the ViewModel
        observeLoginStatus()
    }

    private fun initializeViews() {
        etEmail = findViewById(R.id.emailInput)
        etPassword = findViewById(R.id.passwordInput)
        btnLogin = findViewById(R.id.loginBtn)
        tvForgotPassword = findViewById(R.id.forgotPasswordText)
        tvSignUp = findViewById(R.id.tv_sign_up) // Assuming you have a TextView with this ID
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            loginUser()
        }

        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgetPass::class.java)
            startActivity(intent)
        }

        tvSignUp.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeLoginStatus() {
        viewModel.loginStatus.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                // Navigate to the main screen
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Prevent user from coming back to login screen
            }
            result.onFailure {
                Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
	
    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            etPassword.requestFocus()
            return
        }

        viewModel.loginUser(email, password)
    }
}

