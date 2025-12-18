package com.example.mercuryexperience

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mercuryexperience.ui.theme.MercuryExperienceTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MercuryExperienceTheme {
                SplashScreen(
                    onSplashFinished = {
                        checkAuthenticationStatus()
                    }
                )
            }
        }
    }

    private fun checkAuthenticationStatus() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // Check if user is logged in
        if (currentUser != null) {
            // User is logged in, navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // User is not logged in, navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = FastOutSlowInEasing
        ), label = "alpha"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = FastOutSlowInEasing
        ), label = "scale"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(5000)
        onSplashFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.splashbackgroung),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
        ) {
            // Splash Image
            Image(
                painter = painterResource(id = R.drawable.splashpic),
                contentDescription = "Mercury Experience Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 24.dp),
                contentScale = ContentScale.Fit
            )

            // App Name
            Text(
                text = "MERCURY",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 4.sp
            )

            Text(
                text = "EXPERIENCE",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFe94560),
                letterSpacing = 4.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Tagline
            Text(
                text = "Your Gateway to Entertainment",
                fontSize = 16.sp,
                color = Color(0xFFa8dadc),
                fontWeight = FontWeight.Light,
                letterSpacing = 1.sp
            )
        }

        // Loading indicator at bottom
        Text(
            text = "Loading...",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(alphaAnim.value)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    MercuryExperienceTheme {
        SplashScreen(onSplashFinished = {})
    }
}