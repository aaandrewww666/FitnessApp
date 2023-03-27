package com.example.fitnessapp.ui.data

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class WeightData(var uid : String = "",
                      var weight : Int = 0,
                      var data: String? = null) : Serializable