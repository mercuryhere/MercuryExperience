package com.example.mercuryexperience.ui.app.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mercuryexperience.MainActivity
import com.example.mercuryexperience.R
import com.example.mercuryexperience.data.app.repository.AuthRepository
import com.example.mercuryexperience.utils.Validators
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var tvGoToRegister: TextView

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        tvGoToRegister = findViewById(R.id.tvGoToRegister)

        btnLogin.setOnClickListener {
            loginUser()
        }

        tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        tvForgotPassword.setOnClickListener {
            sendResetEmail()
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        when {
            !Validators.isValidEmail(email) -> {
                etEmail.error = "Enter a valid email"
                etEmail.requestFocus()
            }

            !Validators.isValidPassword(password) -> {
                etPassword.error = "Password must be at least 6 characters"
                etPassword.requestFocus()
            }

            else -> {
                btnLogin.isEnabled = false

                lifecycleScope.launch {
                    val result = authRepository.login(email, password)

                    result.onSuccess {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }.onFailure { error ->
                        btnLogin.isEnabled = true
                        Toast.makeText(
                            this@LoginActivity,
                            error.message ?: "Login failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun sendResetEmail() {
        val email = etEmail.text.toString().trim()

        if (!Validators.isValidEmail(email)) {
            etEmail.error = "Enter a valid email first"
            etEmail.requestFocus()
            return
        }

        lifecycleScope.launch {
            val result = authRepository.sendPasswordReset(email)

            result.onSuccess {
                Toast.makeText(
                    this@LoginActivity,
                    "Password reset email sent",
                    Toast.LENGTH_LONG
                ).show()
            }.onFailure { error ->
                Toast.makeText(
                    this@LoginActivity,
                    error.message ?: "Failed to send reset email",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}