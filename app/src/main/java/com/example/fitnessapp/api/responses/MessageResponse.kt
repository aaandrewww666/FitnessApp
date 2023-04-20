package com.example.fitnessapp.api.responses

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MessageResponse(
    @SerialName("message") val message: String
)
