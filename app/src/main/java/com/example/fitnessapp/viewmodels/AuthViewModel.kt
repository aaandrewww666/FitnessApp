package com.example.fitnessapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnessapp.api.repositories.auth.AuthRepository
import com.example.fitnessapp.api.repositories.userdata.DataRepository
import com.example.fitnessapp.data.models.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataRepository: DataRepository
): ViewModel() {
    private val _state = MutableLiveData<State>()
    val state : LiveData<State> get() = _state

    init {
        _state.postValue(State())
    }

    fun signUp(signUpLogin: String, signUpUsername: String, signUpPassword: String) = flow {
        val result = authRepository.signUp(
            userLogin = signUpLogin,
            username = signUpUsername,
            password = signUpPassword
        )
        emit(result)
    }.flowOn(Dispatchers.IO)

    fun changeState(checking: Boolean) {
        _state.value = _state.value!!.copy(checking = checking)
    }

    fun checkUserData() = flow {
        val result = dataRepository.getUserData()
        emit(result)
    }.flowOn(Dispatchers.IO)

    fun signIn(signInLogin: String, signInPassword: String) = flow {
        val result = authRepository.signIn(
            userLogin = signInLogin,
            password = signInPassword

        )
        emit(result)
    }.flowOn(Dispatchers.IO)

    fun authenticate()  = flow {
            val result = authRepository.authenticate()
        emit(result)
    }.flowOn(Dispatchers.IO)
}