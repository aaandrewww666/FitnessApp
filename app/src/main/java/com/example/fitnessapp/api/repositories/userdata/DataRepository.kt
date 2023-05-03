package com.example.fitnessapp.api.repositories.userdata

import com.example.fitnessapp.api.requests.UserDataRequest
import com.example.fitnessapp.api.results.DataResult

interface DataRepository {
    suspend fun getUserData(): DataResult
    suspend fun updateUserData(userData: UserDataRequest): DataResult
    suspend fun addUserData(userData: UserDataRequest): DataResult
}