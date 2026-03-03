package com.example.mercuryexperience.ui.app.favorites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.mercuryexperience.R
import com.example.mercuryexperience.data.app.model.Movie
import com.example.mercuryexperience.data.app.repository.FavoriteRepository
import com.example.mercuryexperience.ui.app.Details.MovieDetailActivity
import com.example.mercuryexperience.ui.app.home.adapter.MovieAdapter
import com.example.mercuryexperience.utils.Constants
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private val favoriteRepository = FavoriteRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val rvFavorites = findViewById<RecyclerView>(R.id.rvFavorites)
        val btnBackHome = findViewById<Button>(R.id.btnBackHome)

        val allMovies = listOf(
            Movie(
                movieId = "1",
                title = "Interstellar",
                category = "Sci-Fi",
                description = "A science fiction journey through space and time.",
                year = "2014",
                ratingAverage = 4.8
            ),
            Movie(
                movieId = "2",
                title = "Inception",
                category = "Thriller",
                description = "A mind-bending thriller about dreams within dreams.",
                year = "2010",
                ratingAverage = 4.7
            ),
            Movie(
                movieId = "3",
                title = "The Dark Knight",
                category = "Action",
                description = "Batman faces the Joker in a battle for Gotham.",
                year = "2008",
                ratingAverage = 4.9
            ),
            Movie(
                movieId = "4",
                title = "Avatar",
                category = "Adventure",
                description = "A visually stunning journey to the world of Pandora.",
                year = "2009",
                ratingAverage = 4.5
            )
        )

        lifecycleScope.launch {
            val result = favoriteRepository.getFavoriteMovieIds()

            result.onSuccess { favoriteIds ->
                val favoriteMovies = allMovies.filter { it.movieId in favoriteIds }

                val adapter = MovieAdapter(favoriteMovies) { movie ->
                    val intent = Intent(this@FavoritesActivity, MovieDetailActivity::class.java).apply {
                        putExtra(Constants.EXTRA_MOVIE_ID, movie.movieId)
                        putExtra(Constants.EXTRA_MOVIE_TITLE, movie.title)
                        putExtra(Constants.EXTRA_MOVIE_CATEGORY, movie.category)
                        putExtra(Constants.EXTRA_MOVIE_DESCRIPTION, movie.description)
                        putExtra(Constants.EXTRA_MOVIE_YEAR, movie.year)
                        putExtra(Constants.EXTRA_MOVIE_RATING, movie.ratingAverage)
                    }
                    startActivity(intent)
                }

                rvFavorites.adapter = adapter

                if (favoriteMovies.isEmpty()) {
                    Toast.makeText(
                        this@FavoritesActivity,
                        "No favorite movies yet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.onFailure { error ->
                Toast.makeText(
                    this@FavoritesActivity,
                    error.message ?: "Failed to load favorites",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        btnBackHome.setOnClickListener {
            finish()
        }
    }
}
//text