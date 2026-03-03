package com.example.mercuryexperience.ui.app.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mercuryexperience.R
import com.example.mercuryexperience.data.app.model.Movie

class MovieAdapter(
    private val movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMovieTitle: TextView = itemView.findViewById(R.id.tvMovieTitle)
        val tvMovieCategoryYear: TextView = itemView.findViewById(R.id.tvMovieCategoryYear)
        val tvMovieDescription: TextView = itemView.findViewById(R.id.tvMovieDescription)
        val tvMovieRating: TextView = itemView.findViewById(R.id.tvMovieRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.tvMovieTitle.text = movie.title
        holder.tvMovieCategoryYear.text = "${movie.category} • ${movie.year}"
        holder.tvMovieDescription.text = movie.description
        holder.tvMovieRating.text = "Rating: ${movie.ratingAverage}"

        holder.itemView.setOnClickListener {
            onMovieClick(movie)
        }
    }

    override fun getItemCount(): Int = movies.size
}