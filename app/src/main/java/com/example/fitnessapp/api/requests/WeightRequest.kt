package com.example.fitnessapp.api.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeightRequest(
    @SerialName("userWeight") val userWeight: Double,
    @SerialName("date") val date: String
)
