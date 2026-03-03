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
import com.example.mercuryexperience.data.app.model.User
import com.example.mercuryexperience.data.app.repository.AuthRepository
import com.example.mercuryexperience.data.app.repository.UserRepository
import com.example.mercuryexperience.utils.Validators
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvGoToLogin: TextView

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvGoToLogin = findViewById(R.id.tvGoToLogin)

        btnRegister.setOnClickListener {
            registerUser()
        }

        tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val fullName = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        when {
            !Validators.isValidFullName(fullName) -> {
                etFullName.error = "Enter a valid full name"
                etFullName.requestFocus()
            }

            !Validators.isValidEmail(email) -> {
                etEmail.error = "Enter a valid email"
                etEmail.requestFocus()
            }

            !Validators.isValidPassword(password) -> {
                etPassword.error = "Password must be at least 6 characters"
                etPassword.requestFocus()
            }

            !Validators.doPasswordsMatch(password, confirmPassword) -> {
                etConfirmPassword.error = "Passwords do not match"
                etConfirmPassword.requestFocus()
            }

            else -> {
                btnRegister.isEnabled = false

                lifecycleScope.launch {
                    val registerResult = authRepository.register(email, password)

                    registerResult.onSuccess { firebaseUser ->
                        val user = User(
                            uid = firebaseUser.uid,
                            fullName = fullName,
                            email = email,
                            joinedAt = System.currentTimeMillis()
                        )

                        val saveResult = userRepository.saveUser(user)

                        saveResult.onSuccess {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Registration successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                            finish()
                        }.onFailure { error ->
                            btnRegister.isEnabled = true
                            Toast.makeText(
                                this@RegistrationActivity,
                                error.message ?: "Failed to save user",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }.onFailure { error ->
                        btnRegister.isEnabled = true
                        Toast.makeText(
                            this@RegistrationActivity,
                            error.message ?: "Registration failed",
                            Toast.LENGTH_LONG
                        ).show()
                    } //show
                }
            }
        }
    }
}