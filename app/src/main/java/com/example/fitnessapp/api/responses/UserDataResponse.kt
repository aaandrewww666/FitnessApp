package com.example.fitnessapp.api.responses

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserDataResponse(
    @SerialName("height") val height: Double,
    @SerialName("gender") val gender: Int,
    @SerialName("age") val age: Int
)
