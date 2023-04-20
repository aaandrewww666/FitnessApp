package com.example.fitnessapp.api.repositories.weight

import com.example.fitnessapp.api.requests.WeightRequest
import com.example.fitnessapp.api.results.WeightResult

interface UserWeightRepository {
    suspend fun getUserWeight(): WeightResult
    suspend fun addUserWeight(userWeight: WeightRequest): WeightResult
}