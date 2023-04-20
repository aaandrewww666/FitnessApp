package com.example.fitnessapp.api.results

import com.example.fitnessapp.api.responses.UserDataResponse

sealed class DataResult {
    data class Successful(val data: UserDataResponse): DataResult()
    object Posted: DataResult()
    object NotFound: DataResult()
    data class Error(val errorMessage: String? = null): DataResult()
}
