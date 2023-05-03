package com.example.fitnessapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnessapp.api.repositories.weight.UserWeightRepository
import com.example.fitnessapp.api.requests.WeightRequest
import com.example.fitnessapp.api.results.WeightResult
import com.example.fitnessapp.data.models.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val weightRepository: UserWeightRepository
): ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    private val weightListChannel = Channel<WeightResult>()
    val weight = weightListChannel.receiveAsFlow()

    init {
        _state.postValue(State())
    }

    fun changeState(checking: Boolean) {
        _state.value = _state.value!!.copy(checking = checking)
    }

    suspend fun getUserWeights() {
        val result = weightRepository.getUserWeight()
        weightListChannel.send(result)
    }

    suspend fun addUserWeight(weight: Double, date: String) = flow {
        val result = weightRepository.addUserWeight(
            WeightRequest(
                userWeight = weight,
                date = date
            )
        )
        emit(result)
    }.flowOn(Dispatchers.IO)
}