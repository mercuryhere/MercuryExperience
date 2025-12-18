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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MercuryExperienceTheme {
                LoginBody()
            }
        }
    }
}

@Composable
fun LoginBody() {
    val auth = remember { FirebaseAuth.getInstance() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                "Sign In",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "Welcome to Mercury Experience. Sign in to explore amazing entertainment experiences.",
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
                    modifier = Modifier
                        .height(60.dp)
                        .weight(1f),
                    image = R.drawable.facebook,
                    label = "Facebook",
                    enabled = !isLoading,
                    onClick = {
                        Toast.makeText(
                            context,
                            "Facebook login coming soon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                Spacer(modifier = Modifier.width(20.dp))

                SocialMediaCard(
                    modifier = Modifier
                        .height(60.dp)
                        .weight(1f),
                    image = R.drawable.gmail,
                    label = "Gmail",
                    enabled = !isLoading,
                    onClick = {
                        Toast.makeText(
                            context,
                            "Google login coming soon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(R.drawable.baseline_visibility_24)
                            else
                                painterResource(R.drawable.baseline_visibility_off_24),
                            contentDescription = if (visibility) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (visibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("********") },
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

            Text(
                "Forgot password?",
                style = TextStyle(
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 15.dp)
                    .clickable(enabled = !isLoading) {
                        // Handle forgot password
                        if (email.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please enter your email address",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Password reset email sent",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Failed to send reset email: ${task.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
            )

            Button(
                onClick = {
                    // Validate input
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Please enter email and password",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    isLoading = true

                    // Login with Firebase
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Login successful",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Save email to SharedPreferences
                                val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
                                sharedPreferences.edit().putString("email", email).apply()

                                // Navigate to main activity
                                val intent = Intent(context, MainActivity::class.java)
                                intent.putExtra("email", email)
                                context.startActivity(intent)
                                activity.finish()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Login failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                    Text("Log In")
                }
            }

            Text(
                buildAnnotatedString {
                    append("Don't have an account? ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Sign up")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clickable(enabled = !isLoading) {
                        val intent = Intent(context, RegistrationActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    },
                style = TextStyle(textAlign = TextAlign.Center)
            )
        }
    }
}

@Composable
fun SocialMediaCard(
    modifier: Modifier,
    image: Int,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick,
        enabled = enabled
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
fun PreviewLogin() {
    MercuryExperienceTheme {
        LoginBody()
    }
}