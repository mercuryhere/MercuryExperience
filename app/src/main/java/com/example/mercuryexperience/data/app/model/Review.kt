package com.example.mercuryexperience.data.app.model

data class Review(
    val reviewId: String = "",
    val movieId: String = "",
    val userId: String = "",
    val userName: String = "",
    val stars: Int = 0,
    val comment: String = "",
    val createdAt: Long = 0L
)
//text