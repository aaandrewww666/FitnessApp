package com.example.fitnessapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.api.repositories.userdata.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.lifecycle.ViewModel
import com.example.fitnessapp.api.repositories.weight.UserWeightRepository
import com.example.fitnessapp.api.requests.UserDataRequest
import com.example.fitnessapp.api.requests.WeightRequest
import com.example.fitnessapp.api.results.DataResult
import com.example.fitnessapp.data.models.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@HiltViewModel
class PhysicalIndexesViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val weightRepository: UserWeightRepository
): ViewModel() {
    private val _state = MutableLiveData<State>()
    val state : LiveData<State> get() = _state

    private val _ageValue = MutableLiveData<Int>()
    val ageValue: LiveData<Int> get() = _ageValue


    private val _weightValue = MutableLiveData<Int>()
    val weightValue: LiveData<Int> get() = _weightValue


    private val _heightValue = MutableLiveData<Int>()
    val heightValue: LiveData<Int> get() = _heightValue

    init {
        _state.postValue(State())
    }

    fun updateAge(age: Int) {
        _ageValue.value = age
    }

    fun updateWeight(weight: Int) {
        _weightValue.value = weight
    }

    fun updateHeight(height: Int) {
        _heightValue.value = height
    }

    fun changeState(checking: Boolean) {
        _state.value = _state.value!!.copy(checking = checking)
    }

    fun addUserData(age: Int, gender: Int, height: Double): Flow<DataResult> = flow {
        val result = dataRepository.addUserData(
            UserDataRequest(
                age = age,
                gender = gender,
                height = height
            )
        )
        emit(result)
    }.flowOn(Dispatchers.IO)


    fun addUserWeight(weight: Double, date: String) = flow {
        val result = weightRepository.addUserWeight(
            WeightRequest(
                userWeight = weight,
                date = date
            )
        )
        emit(result)
    }.flowOn(Dispatchers.IO)
}