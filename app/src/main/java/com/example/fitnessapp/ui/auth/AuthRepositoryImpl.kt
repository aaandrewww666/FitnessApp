package com.example.fitnessapp.ui.auth

import android.content.SharedPreferences
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val prefs: SharedPreferences
): AuthRepository {
    override suspend fun singUp(
        userLogin: String,
        password: String,
        username: String
    ): AuthResult<Unit> {
        return try {
            api.singUp(
                request = SingUpRequest(
                    userLogin = userLogin,
                    password = password,
                    username = username
                )
            )
            singIn(userLogin, password)
        } catch (e: HttpException) {
            if(e.code() == 401){
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun singIn(userLogin: String, password: String): AuthResult<Unit> {
        return try {
            val response = api.singIn(
                request = AuthRequest(
                    userLogin = userLogin,
                    password = password
                )
            )
            prefs.edit()
                .putString("jwt", response.token)
                .apply()
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if(e.code() == 401){
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if(e.code() == 401){
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

}