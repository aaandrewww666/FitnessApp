package com.example.fitnessapp.api.results

sealed class PostResponseResult {
    object Successful : PostResponseResult()
    data class Error(val message: String): PostResponseResult()
}
