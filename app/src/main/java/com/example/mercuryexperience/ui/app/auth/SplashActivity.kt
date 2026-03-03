package com.example.mercuryexperience.ui.app.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.mercuryexperience.MainActivity
import com.example.mercuryexperience.R
import com.example.mercuryexperience.data.app.repository.AuthRepository

class SplashActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val nextScreen = if (authRepository.getCurrentUser() != null) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }

            startActivity(nextScreen)
            finish()
        }, 1500) //sp
    }
}
//text