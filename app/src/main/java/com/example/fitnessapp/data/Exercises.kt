package com.example.fitnessapp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Exercises(
    @SerializedName("bodyPart") @Expose var bodyPart: String? = null,
    @SerializedName("equipment") @Expose var equipment: String? = null,
    @SerializedName("gifUrl") @Expose var gifUrl: String? = null,
    @SerializedName("id") @Expose var id: String? = null,
    @SerializedName("name") @Expose val name: String? = null,
    @SerializedName("target") @Expose val target: String? = null
)