package com.example.fitnessapp.api.repositories.userdata

import android.content.SharedPreferences
import android.util.Log
import com.example.fitnessapp.api.Api
import com.example.fitnessapp.api.requests.UserDataRequest
import com.example.fitnessapp.api.responses.MessageResponse
import com.example.fitnessapp.api.responses.UserDataResponse
import com.example.fitnessapp.api.results.DataResult
import com.example.fitnessapp.api.results.WeightResult
import com.google.gson.Gson
import java.net.SocketTimeoutException

class DataRepositoryImpl(
    private val api: Api,
    private val prefs: SharedPreferences
): DataRepository {
    override suspend fun getUserData(): DataResult {
        return try {
            val token = prefs.getString("jwt", null)
            val response = api.getUserData(
                token = "Bearer $token"
            )
            if(response.code() == 200) {
                DataResult.Successful(UserDataResponse(
                    height = response.body()!!.height,
                    age = response.body()!!.age,
                    gender = response.body()!!.gender
                    ))
            } else if (response.code() == 404) {
                    DataResult.NotFound
                } else {
                    DataResult.Error(response.errorBody()!!.string())
                }

        } catch (e: SocketTimeoutException) {
            DataResult.Error("Не удаётся подключиться к серверу")
        }
        catch (e: Exception) {
            DataResult.Error("Что-то не так...")
        }
    }

    override suspend fun updateUserData(userData: UserDataRequest): DataResult {
        return try {
            val token = prefs.getString("jwt", null)
            val response = api.updateUserData(
                token = "Bearer $token",
                request = userData
            )
            Log.i("sssssss",response.code().toString())
            Log.i("sssssss",response.errorBody().toString())
            if(response.code() == 200) {
                DataResult.Successful(null)
            } else {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()!!.string(),
                    MessageResponse::class.java
                )
                DataResult.Error(error.message)
            }
        } catch (e: SocketTimeoutException) {
            DataResult.Error("Не удаётся подключиться к серверу")
        }
        catch (e: Exception) {
            DataResult.Error("Что-то не так...")
        }
    }

    override suspend fun addUserData(userData: UserDataRequest): DataResult {
        return try {
            val token = prefs.getString("jwt", null)
            val response = api.addUserData(
                token = "Bearer $token",
                request = userData
            )
            if(response.code() == 201) {
                DataResult.Posted
            } else {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()!!.string(),
                    MessageResponse::class.java
                )
                DataResult.Error(error.message)
            }
        } catch (e: SocketTimeoutException) {
            DataResult.Error("Не удаётся подключиться к серверу")
        }
        catch (e: Exception) {
            DataResult.Error("Что-то не так...")
        }
    }

}