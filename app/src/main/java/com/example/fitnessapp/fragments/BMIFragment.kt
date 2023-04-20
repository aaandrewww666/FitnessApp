package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitnessapp.databinding.FragmentBMIBinding

class BMIFragment : Fragment() {
    private var _binding: FragmentBMIBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBMIBinding.inflate(inflater)
        var height : String
        var gender : String
        var age : String
        /*FireStore.firebaseStore.collection("usersInformation")
            .document(FireAuth.firebaseAuth.uid.toString())
            .get()
            .addOnSuccessListener { document ->

                height = document.get("height").toString()
                gender = if (document.get("gender").toString() == "M") {
                    if(Locale.getDefault().country != "RU") {
                        "Male"
                    } else {
                        "Мужской"
                    }

                } else {
                    if(Locale.getDefault().country != "RU") {
                        "Female"
                    } else {
                        "Женский"
                    }
                }
                age = document.get("age").toString()
                var averageWeight = 0.0
                dataModel.getUserWeight.observe(activity as LifecycleOwner) {
                    val wd = it
                    wd.forEach {
                        averageWeight += it.weight
                    }
                    averageWeight /= wd.size
                    binding.textViewGenderShow.text = gender
                    binding.textViewHeightShow.text = height
                    binding.textViewAgeShow.text = age
                    binding.textViewWeight.text = "%.3f".format(averageWeight.toFloat()).replace(",",".")
                    val resultBMI = calculate(height.toDouble(), averageWeight)
                    binding.textViewBMIResult.text = "%.3f".format(resultBMI.toFloat()).replace(",",".")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("sss", "Error getting documents: ", exception)
            }
*/
        return binding.root
    }

    private fun calculate(height : Double, weight : Double) : Double {
        return weight/(height * height/10000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}