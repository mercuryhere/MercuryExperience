package com.example.mercuryexperience.data.app.repository

import com.example.mercuryexperience.data.app.firebase.FirebaseRefs
import com.example.mercuryexperience.data.app.model.Review
import kotlinx.coroutines.tasks.await

class ReviewRepository {

    suspend fun addReview(review: Review): Result<Unit> {
        return try {
            FirebaseRefs.reviews
                .child(review.movieId)
                .child(review.reviewId)
                .setValue(review)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsForMovie(movieId: String): Result<List<Review>> {
        return try {
            val snapshot = FirebaseRefs.reviews
                .child(movieId)
                .get()
                .await()

            val reviews = mutableListOf<Review>()
            for (child in snapshot.children) {
                val review = child.getValue(Review::class.java)
                if (review != null) {
                    reviews.add(review)
                }
            }

            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
//text