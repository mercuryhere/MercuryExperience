package com.example.mercuryexperience

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mercuryexperience.data.app.model.Movie
import com.example.mercuryexperience.data.app.repository.AuthRepository
import com.example.mercuryexperience.ui.app.auth.LoginActivity
import com.example.mercuryexperience.ui.app.Details.MovieDetailActivity
import com.example.mercuryexperience.ui.app.favorites.FavoritesActivity
import com.example.mercuryexperience.ui.app.home.adapter.MovieAdapter
import com.example.mercuryexperience.utils.Constants

class MainActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val rvMovies = findViewById<RecyclerView>(R.id.rvMovies)
        val btnFavorites = findViewById<Button>(R.id.btnFavorites)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val currentUser = authRepository.getCurrentUser()
        val email = currentUser?.email ?: "User"
        tvWelcome.text = "Welcome, $email"

        val movieList = listOf(
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

        val adapter = MovieAdapter(movieList) { movie ->
            val intent = Intent(this, MovieDetailActivity::class.java).apply {
                putExtra(Constants.EXTRA_MOVIE_ID, movie.movieId)
                putExtra(Constants.EXTRA_MOVIE_TITLE, movie.title)
                putExtra(Constants.EXTRA_MOVIE_CATEGORY, movie.category)
                putExtra(Constants.EXTRA_MOVIE_DESCRIPTION, movie.description)
                putExtra(Constants.EXTRA_MOVIE_YEAR, movie.year)
                putExtra(Constants.EXTRA_MOVIE_RATING, movie.ratingAverage)
            }
            startActivity(intent)
        }

        rvMovies.adapter = adapter

        btnFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        btnLogout.setOnClickListener {
            authRepository.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}