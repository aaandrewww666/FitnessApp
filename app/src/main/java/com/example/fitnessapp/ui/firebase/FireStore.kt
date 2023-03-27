package com.example.fitnessapp.ui.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FireStore {
    val firebaseStore by lazy {
        Firebase.firestore
    }
}