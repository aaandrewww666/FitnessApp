package com.example.fitnessapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentPhysicalIndexesBinding
import com.example.fitnessapp.ui.data.Person
import com.example.fitnessapp.ui.data.WeightData
import com.example.fitnessapp.ui.firebase.FireAuth.firebaseAuth
import com.example.fitnessapp.ui.firebase.FireStore.firebaseStore
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class PhysicalIndexesFragment : Fragment() {

    private var _binding: FragmentPhysicalIndexesBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhysicalIndexesBinding.inflate(layoutInflater)
        binding.continueButton.setOnClickListener {
            if (
                binding.ageEdittext.text.isNotEmpty() &&
                binding.weightEdittext.text.isNotEmpty() &&
                binding.heightEdittext.text.isNotEmpty()
            ) {
                val age = binding.ageEdittext.text.toString().toInt()
                val height = binding.heightEdittext.text.toString().toInt()
                var gender = "M"
                when (binding.radiogroupGender.checkedRadioButtonId) {
                    R.id.female_radiobutton -> gender = "F"
                }
                val weight = binding.weightEdittext.text.toString().toInt()
                if (
                    age in 10..99 &&
                    height in 100..250 &&
                    weight in 20..200
                ) {
                    val hmp = HashMap<String, Int>()
                    val uid = firebaseAuth.uid.toString()
                    hmp[uid] = 1
                    firebaseStore.collection("usersEntries")
                        .document(uid)
                        .set(hmp)
                        .addOnSuccessListener {
                            firebaseStore.collection("usersInformation")
                                .document(uid)
                                .set(
                                    Person(
                                        gender,
                                        height,
                                        age
                                    )
                                )
                                .addOnSuccessListener {
                                    firebaseStore.collection("usersWeightInformation")
                                        .document()
                                        .set(WeightData(uid,weight, LocalDateTime.now()
                                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))))
                                        .addOnSuccessListener {
                                            navController.navigate(R.id.action_physicalIndexesFragment_to_mainFragment)
                                        }
                                }
                        }
                } else {
                    if(Locale.getDefault().country != "RU") {
                        showSnackbar(binding.root, "Write correct data")
                    } else {
                        showSnackbar(binding.root, "Введите корректные данные")
                    }
                }
            } else {
                if(Locale.getDefault().country != "RU") {
                    showSnackbar(binding.root, "Fill the fields")
                } else {
                    showSnackbar(binding.root, "Заполните поля")
                }
            }
        }
        return binding.root
    }

    private fun showSnackbar(view : View, text : String) {
        Snackbar.make(
            view,
            text,
            Snackbar.LENGTH_SHORT
        )
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}