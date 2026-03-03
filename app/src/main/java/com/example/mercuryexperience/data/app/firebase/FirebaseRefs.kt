package com.example.mercuryexperience.data.app.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseRefs {

    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    val users: DatabaseReference by lazy {
        database.child("users")
    }

    val movies: DatabaseReference by lazy {
        database.child("movies") //hello
    }

    val reviews: DatabaseReference by lazy {
        database.child("reviews")
    }

    val favorites: DatabaseReference by lazy {
        database.child("favorites")
    }
}