package com.example.mercuryexperience.data.app.repository

import com.example.mercuryexperience.data.app.firebase.FirebaseRefs
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository {

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseRefs.auth.currentUser
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = FirebaseRefs.auth
                .signInWithEmailAndPassword(email, password)
                .await()

            val user = authResult.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = FirebaseRefs.auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val user = authResult.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            FirebaseRefs.auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        FirebaseRefs.auth.signOut()
    }
}
//text