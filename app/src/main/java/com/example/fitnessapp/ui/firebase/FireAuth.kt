package com.example.fitnessapp.ui.firebase

import com.google.firebase.auth.FirebaseAuth

object FireAuth {
    val firebaseAuth : FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
}