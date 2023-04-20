package com.example.fitnessapp.api.results

import com.example.fitnessapp.api.responses.WeightResponse

sealed class WeightResult {
    data class Successful(val data: WeightResponse): WeightResult()
    object Posted: WeightResult()
    object NotFound: WeightResult()
    data class Error(val errorMessage: String? = null): WeightResult()
}