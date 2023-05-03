package com.example.fitnessapp.data.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Exercises(
    val classification: String,
    val exerciseName: String,
    val inventory: String,
    val variants: String,
    val coreMuscles: String,
    val additionalMuscles: String,
    val stabilizingMuscles: String,
    val photos: List<String>
)