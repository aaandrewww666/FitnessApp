package com.example.fitnessapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnessapp.ui.data.WeightData
import com.example.fitnessapp.ui.firebase.FireAuth
import com.example.fitnessapp.ui.firebase.FireStore
import com.google.firebase.firestore.ktx.toObject

class ViewModel : ViewModel() {
    val getUserWeight : MutableLiveData<ArrayList<WeightData>> = MutableLiveData<ArrayList<WeightData>>()
    init {
        val weightDataList = ArrayList<WeightData>()
        FireStore.firebaseStore.collection("usersWeightInformation")
            .whereEqualTo("uid", FireAuth.firebaseAuth.uid.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    weightDataList.add(document.toObject())
                }
                getUserWeight.value = weightDataList
                Log.d("sss", weightDataList.toString())
            }
            .addOnFailureListener { exception ->
                Log.d("sss", "Error getting documents: ", exception)
            }
    }

    fun updateUserWeight(date : String, weight : Int) {
        FireStore.firebaseStore.collection("usersWeightInformation")
            .document()
            .set(WeightData(FireAuth.firebaseAuth.uid.toString(), weight, date))
    }
}