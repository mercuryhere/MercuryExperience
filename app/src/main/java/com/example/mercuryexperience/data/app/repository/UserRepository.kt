package com.example.mercuryexperience.data.app.repository

import com.example.mercuryexperience.data.app.firebase.FirebaseRefs
import com.example.mercuryexperience.data.app.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    suspend fun saveUser(user: User): Result<Unit> {
        return try {
            FirebaseRefs.users
                .child(user.uid)
                .setValue(user)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(uid: String): Result<User> {
        return try {
            val snapshot = FirebaseRefs.users
                .child(uid)
                .get()
                .await()

            val user = snapshot.getValue(User::class.java)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}