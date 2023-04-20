package com.example.fitnessapp.api.responses

import com.example.fitnessapp.api.requests.WeightRequest
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class WeightResponse(
    @SerialName("userWeights") val userWeights: List<WeightRequest>
)