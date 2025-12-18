package com.example.mercuryexperience

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mercuryexperience.ui.theme.MercuryExperienceTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MercuryExperienceTheme {
                RegistrationBody()
            }
        }
    }
}

@Composable
fun RegistrationBody() {
    val auth = remember { FirebaseAuth.getInstance() }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val scrollState = rememberScrollState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                "Sign Up",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "Create your Mercury Experience account to explore amazing entertainment and exclusive experiences.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 15.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {
                SocialMediaCard(
                    Modifier
                        .height(60.dp)
                        .weight(1f),
                    R.drawable.facebook,
                    "Facebook"
                )

                Spacer(modifier = Modifier.width(20.dp))

                SocialMediaCard(
                    Modifier
                        .height(60.dp)
                        .weight(1f),
                    R.drawable.gmail,
                    "Gmail"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp, horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    "OR",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            // Full Name Field
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("Full Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                ),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("abc@gmail.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                ),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painter = if (passwordVisibility)
                                painterResource(R.drawable.baseline_visibility_24)
                            else
                                painterResource(R.drawable.baseline_visibility_off_24),
                            contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                ),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                        Icon(
                            painter = if (confirmPasswordVisibility)
                                painterResource(R.drawable.baseline_visibility_24)
                            else
                                painterResource(R.drawable.baseline_visibility_off_24),
                            contentDescription = if (confirmPasswordVisibility) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("Confirm Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                ),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Sign Up Button
            Button(
                onClick = {
                    // Validate input
                    when {
                        fullName.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Please enter your full name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        email.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Please enter your email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        password.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Please enter a password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        password.length < 6 -> {
                            Toast.makeText(
                                context,
                                "Password must be at least 6 characters",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        password != confirmPassword -> {
                            Toast.makeText(
                                context,
                                "Passwords do not match",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            isLoading = true

                            // Register with Firebase
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Update user profile with display name
                                        val user = auth.currentUser
                                        val profileUpdates = UserProfileChangeRequest.Builder()
                                            .setDisplayName(fullName)
                                            .build()

                                        user?.updateProfile(profileUpdates)
                                            ?.addOnCompleteListener { profileTask ->
                                                isLoading = false
                                                if (profileTask.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Registration successful",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    // Save user data to SharedPreferences
                                                    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
                                                    sharedPreferences.edit().apply {
                                                        putString("email", email)
                                                        putString("fullName", fullName)
                                                        apply()
                                                    }

                                                    // Navigate to main activity
                                                    val intent = Intent(context, MainActivity::class.java)
                                                    intent.putExtra("email", email)
                                                    intent.putExtra("fullName", fullName)
                                                    context.startActivity(intent)
                                                    activity.finish()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to update profile: ${profileTask.exception?.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            "Registration failed: ${task.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 15.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Sign Up")
                }
            }

            Text(
                buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Sign in")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clickable(enabled = !isLoading) {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    },
                style = TextStyle(textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun SocialMediaCard(modifier: Modifier, image: Int, label: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(label)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistration() {
    MercuryExperienceTheme {
        RegistrationBody()
    }
}