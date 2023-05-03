package com.example.fitnessapp.data.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PlanarClassification(
    @SerialName("name") val name: String,
    @SerialName("description") val description: String
    )
