package com.example.fitnessapp.ui.model

import com.example.fitnessapp.ui.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
}