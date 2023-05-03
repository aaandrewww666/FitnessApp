package com.example.fitnessapp.api.repositories.weight

import android.content.SharedPreferences
import android.util.Log
import com.example.fitnessapp.api.Api
import com.example.fitnessapp.api.requests.WeightRequest
import com.example.fitnessapp.api.responses.MessageResponse
import com.example.fitnessapp.api.results.WeightResult
import com.google.gson.Gson
import java.net.SocketTimeoutException

class UserWeightRepositoryImpl(
    private val api: Api,
    private val prefs: SharedPreferences
): UserWeightRepository {
    override suspend fun getUserWeight(): WeightResult {
        return try {
            val token = prefs.getString("jwt", null)
            val response = api.getWeights(
                token = "Bearer $token"
            )
            if(response.code() == 200) {
                WeightResult.Successful(response.body()!!)
            } else if (response.code() == 404) {
                WeightResult.NotFound
            } else {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()!!.string(),
                    MessageResponse::class.java
                )
                WeightResult.Error(error.message)
            }
        } catch (e: SocketTimeoutException) {
            WeightResult.Error("Не удаётся подключиться к серверу")
        }
        catch (e: Exception) {
            WeightResult.Error("Что-то не так...")
        }
    }

    override suspend fun addUserWeight(userWeight: WeightRequest): WeightResult {
        return try {
            val token = prefs.getString("jwt", null)
            val response = api.addWeight(
                token = "Bearer $token",
                request = userWeight
            )
            if(response.code() == 201) {
                WeightResult.Posted
            } else {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()!!.string(),
                    MessageResponse::class.java
                )
                WeightResult.Error(error.message)
            }
        } catch (e: SocketTimeoutException) {
            WeightResult.Error("Не удаётся подключиться к серверу")
        }
    }
}