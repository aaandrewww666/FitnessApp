package com.example.fitnessapp.api.repositories.auth

import android.content.SharedPreferences
import com.example.fitnessapp.api.Api
import com.example.fitnessapp.api.requests.SignInRequest
import com.example.fitnessapp.api.requests.SignUpRequest
import com.example.fitnessapp.api.responses.MessageResponse
import com.example.fitnessapp.api.results.AuthResult
import com.example.fitnessapp.api.results.PostResponseResult
import com.google.gson.Gson
import java.net.SocketTimeoutException

class AuthRepositoryImpl(
    private val api: Api,
    private val prefs: SharedPreferences
): AuthRepository {

    override suspend fun signIn(userLogin: String, password: String): AuthResult {
        return try {
            val response = api.singIn(
                request = SignInRequest(
                    userLogin = userLogin,
                    password = password
                )
            )
            if(response.code() == 200) {
                prefs.edit()
                    .putString("jwt", response.body()!!.message)
                    .apply()
                AuthResult.Authorized
            } else {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()!!.string(),
                    MessageResponse::class.java
                )
                AuthResult.Error(error.message)
            }
        } catch (e: SocketTimeoutException) {
            AuthResult.Error("Не удаётся подключиться к серверу")
        }
        catch (e: Exception) {
            AuthResult.Error("Что-то не так...")
        }
    }

    override suspend fun signUp(
        userLogin: String,
        password: String,
        username: String
    ): PostResponseResult {
        return try {
            val response = api.singUp(
                request = SignUpRequest(
                    userLogin = userLogin,
                    password = password,
                    username = username
                )
            )

            if(response.code() == 201) {
                PostResponseResult.Successful
            } else {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()!!.string(),
                    MessageResponse::class.java
                )
                PostResponseResult.Error(error.message)
            }
        } catch (e: SocketTimeoutException) {
            PostResponseResult.Error("Не удаётся подключиться к серверу")
        }
        catch (e: Exception) {
            PostResponseResult.Error("Что-то не так...")
        }
    }

    override suspend fun authenticate(): AuthResult {
        return try {
            val token = prefs
                .getString("jwt", null)
                ?: return AuthResult.Unauthorized
            val response = api.authenticate("Bearer $token")
            if(response.code() == 200) {
                AuthResult.Authorized
            } else if(response.code() == 401) {
                AuthResult.Unauthorized
            } else {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()!!.string(),
                    MessageResponse::class.java
                )
                AuthResult.Error(error.message)
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message)
        }
    }
}