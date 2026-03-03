package com.example.mercuryexperience.ui.app.Details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mercuryexperience.R
import com.example.mercuryexperience.data.app.model.Review

class ReviewAdapter(
    private val reviews: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvReviewerName: TextView = itemView.findViewById(R.id.tvReviewerName)
        val tvReviewStars: TextView = itemView.findViewById(R.id.tvReviewStars)
        val tvReviewComment: TextView = itemView.findViewById(R.id.tvReviewComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.tvReviewerName.text = review.userName
        holder.tvReviewStars.text = "Rating: ${review.stars}/5"
        holder.tvReviewComment.text = review.comment
    }

    override fun getItemCount(): Int = reviews.size
}