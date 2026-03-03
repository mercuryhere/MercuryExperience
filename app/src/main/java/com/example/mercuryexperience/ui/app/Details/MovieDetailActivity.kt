package com.example.mercuryexperience.ui.app.Details

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.mercuryexperience.R
import com.example.mercuryexperience.data.app.repository.FavoriteRepository
import com.example.mercuryexperience.data.app.repository.ReviewRepository
import com.example.mercuryexperience.ui.app.Details.adapter.ReviewAdapter
import com.example.mercuryexperience.ui.app.review.AddReviewActivity
import com.example.mercuryexperience.utils.Constants
import kotlinx.coroutines.launch

class MovieDetailActivity : AppCompatActivity() {

    private val favoriteRepository = FavoriteRepository()
    private val reviewRepository = ReviewRepository()

    private var movieId: String = ""
    private var isFavoriteMovie: Boolean = false
    private lateinit var rvReviews: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvCategoryYear = findViewById<TextView>(R.id.tvDetailCategoryYear)
        val tvRating = findViewById<TextView>(R.id.tvDetailRating)
        val tvDescription = findViewById<TextView>(R.id.tvDetailDescription)
        val btnAddReview = findViewById<Button>(R.id.btnAddReview)
        val btnFavorite = findViewById<Button>(R.id.btnFavorite)
        val btnBack = findViewById<Button>(R.id.btnBack)
        rvReviews = findViewById(R.id.rvReviews)

        movieId = intent.getStringExtra(Constants.EXTRA_MOVIE_ID) ?: ""
        val title = intent.getStringExtra(Constants.EXTRA_MOVIE_TITLE) ?: "Unknown Title"
        val category = intent.getStringExtra(Constants.EXTRA_MOVIE_CATEGORY) ?: "Unknown Category"
        val description = intent.getStringExtra(Constants.EXTRA_MOVIE_DESCRIPTION) ?: "No description available"
        val year = intent.getStringExtra(Constants.EXTRA_MOVIE_YEAR) ?: "Unknown Year"
        val rating = intent.getDoubleExtra(Constants.EXTRA_MOVIE_RATING, 0.0)

        tvTitle.text = title
        tvCategoryYear.text = "$category • $year"
        tvRating.text = "Rating: $rating"
        tvDescription.text = description

        lifecycleScope.launch {
            val favoriteResult = favoriteRepository.isFavorite(movieId)
            favoriteResult.onSuccess { favorite ->
                isFavoriteMovie = favorite
                updateFavoriteButtonText(btnFavorite)
            }
        }

        loadReviews()

        btnAddReview.setOnClickListener {
            val intent = Intent(this, AddReviewActivity::class.java).apply {
                putExtra(Constants.EXTRA_MOVIE_ID, movieId)
            }
            startActivity(intent)
        }

        btnFavorite.setOnClickListener {
            toggleFavorite(btnFavorite)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadReviews()
    }

    private fun loadReviews() {
        lifecycleScope.launch {
            val result = reviewRepository.getReviewsForMovie(movieId)
            result.onSuccess { reviews ->
                rvReviews.adapter = ReviewAdapter(reviews)
            }.onFailure { error ->
                Toast.makeText(
                    this@MovieDetailActivity,
                    error.message ?: "Failed to load reviews",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun toggleFavorite(btnFavorite: Button) {
        lifecycleScope.launch {
            if (isFavoriteMovie) {
                val result = favoriteRepository.removeFavorite(movieId)
                result.onSuccess {
                    isFavoriteMovie = false
                    updateFavoriteButtonText(btnFavorite)
                    Toast.makeText(this@MovieDetailActivity, "Removed from favorites", Toast.LENGTH_SHORT).show()
                }.onFailure { error ->
                    Toast.makeText(this@MovieDetailActivity, error.message ?: "Failed to remove favorite", Toast.LENGTH_LONG).show()
                }
            } else {
                val result = favoriteRepository.addFavorite(movieId)
                result.onSuccess {
                    isFavoriteMovie = true
                    updateFavoriteButtonText(btnFavorite)
                    Toast.makeText(this@MovieDetailActivity, "Added to favorites", Toast.LENGTH_SHORT).show()
                }.onFailure { error ->
                    Toast.makeText(this@MovieDetailActivity, error.message ?: "Failed to add favorite", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateFavoriteButtonText(button: Button) {
        button.text = if (isFavoriteMovie) "Remove from Favorites" else "Add to Favorites"
    }
}
//text