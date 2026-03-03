package com.example.mercuryexperience.data.app.repository

import com.example.mercuryexperience.data.app.firebase.FirebaseRefs
import kotlinx.coroutines.tasks.await

class FavoriteRepository {

    fun getCurrentUserId(): String? {
        return FirebaseRefs.auth.currentUser?.uid
    }

    suspend fun addFavorite(movieId: String): Result<Unit> {
        return try {
            val uid = getCurrentUserId() ?: return Result.failure(Exception("User not logged in"))

            FirebaseRefs.favorites
                .child(uid)
                .child(movieId)
                .setValue(true)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFavorite(movieId: String): Result<Unit> {
        return try {
            val uid = getCurrentUserId() ?: return Result.failure(Exception("User not logged in"))

            FirebaseRefs.favorites
                .child(uid)
                .child(movieId)
                .removeValue()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isFavorite(movieId: String): Result<Boolean> {
        return try {
            val uid = getCurrentUserId() ?: return Result.failure(Exception("User not logged in"))

            val snapshot = FirebaseRefs.favorites
                .child(uid)
                .child(movieId)
                .get()
                .await()

            Result.success(snapshot.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavoriteMovieIds(): Result<List<String>> {
        return try {
            val uid = getCurrentUserId() ?: return Result.failure(Exception("User not logged in"))

            val snapshot = FirebaseRefs.favorites
                .child(uid)
                .get()
                .await()

            val favoriteIds = mutableListOf<String>()
            for (child in snapshot.children) {
                child.key?.let { favoriteIds.add(it) }
            }

            Result.success(favoriteIds)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}