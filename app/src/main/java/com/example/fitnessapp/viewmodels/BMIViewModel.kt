package com.example.fitnessapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnessapp.api.repositories.userdata.DataRepository
import com.example.fitnessapp.api.repositories.weight.UserWeightRepository
import com.example.fitnessapp.data.models.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class BMIViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val weightRepository: UserWeightRepository
): ViewModel() {

    private val _state = MutableLiveData<State>()
    val state : LiveData<State> get() = _state

    init {
        _state.postValue(State())
    }

    fun changeState(checking: Boolean) {
        _state.value = _state.value!!.copy(checking = checking)
    }

    fun getUserWeight() = flow {
        val result = weightRepository.getUserWeight()
        emit(result)
    }.flowOn(Dispatchers.IO)

    fun getUserData() = flow {
        val result = dataRepository.getUserData()
        emit(result)
    }.flowOn(Dispatchers.IO)
}