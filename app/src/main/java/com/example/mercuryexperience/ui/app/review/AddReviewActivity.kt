package com.example.mercuryexperience.ui.app.review

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mercuryexperience.R
import com.example.mercuryexperience.data.app.firebase.FirebaseRefs
import com.example.mercuryexperience.data.app.model.Review
import com.example.mercuryexperience.data.app.repository.ReviewRepository
import com.example.mercuryexperience.utils.Constants
import kotlinx.coroutines.launch

class AddReviewActivity : AppCompatActivity() {

    private val reviewRepository = ReviewRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val etComment = findViewById<EditText>(R.id.etComment)
        val btnSubmitReview = findViewById<Button>(R.id.btnSubmitReview)
        val btnCancelReview = findViewById<Button>(R.id.btnCancelReview)

        val movieId = intent.getStringExtra(Constants.EXTRA_MOVIE_ID) ?: ""

        btnSubmitReview.setOnClickListener {
            val stars = ratingBar.rating.toInt()
            val comment = etComment.text.toString().trim()

            when {
                movieId.isBlank() -> {
                    Toast.makeText(this, "Invalid movie", Toast.LENGTH_LONG).show()
                }

                stars !in 1..5 -> {
                    Toast.makeText(this, "Please select a rating", Toast.LENGTH_LONG).show()
                }

                comment.length < 3 -> {
                    etComment.error = "Comment must be at least 3 characters"
                    etComment.requestFocus()
                }

                else -> {
                    saveReview(movieId, stars, comment, btnSubmitReview)
                }
            }
        }

        btnCancelReview.setOnClickListener {
            finish()
        }
    }

    private fun saveReview(
        movieId: String,
        stars: Int,
        comment: String,
        btnSubmitReview: Button
    ) {
        val currentUser = FirebaseRefs.auth.currentUser

        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in", Toast.LENGTH_LONG).show()
            return
        }

        btnSubmitReview.isEnabled = false

        val reviewId = FirebaseRefs.reviews.child(movieId).push().key ?: System.currentTimeMillis().toString()

        val review = Review(
            reviewId = reviewId,
            movieId = movieId,
            userId = currentUser.uid,
            userName = currentUser.email ?: "Anonymous",
            stars = stars,
            comment = comment,
            createdAt = System.currentTimeMillis()
        )

        lifecycleScope.launch {
            val result = reviewRepository.addReview(review)

            result.onSuccess {
                Toast.makeText(
                    this@AddReviewActivity,
                    "Review added successfully",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }.onFailure { error ->
                btnSubmitReview.isEnabled = true
                Toast.makeText(
                    this@AddReviewActivity,
                    error.message ?: "Failed to add review",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
//text