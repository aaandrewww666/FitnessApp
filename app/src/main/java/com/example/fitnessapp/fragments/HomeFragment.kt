package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        binding.buttonExercises.setOnClickListener {
            loadFragment(BodypartsFragment(), "exercises")
        }
        binding.buttonFood.setOnClickListener {
            loadFragment(FoodFragment(), "food")
        }
        binding.buttonBMI.setOnClickListener {
            loadFragment(BMIFragment(), "BMI")
        }
        binding.buttonWaterConsumption.setOnClickListener {
            loadFragment(WaterConsumptionFragment(), "waterCons")
        }
        return binding.root
    }

    private fun loadFragment(fragment: Fragment, backStackName : String){
        parentFragmentManager.clearBackStack("exercises")
        parentFragmentManager.clearBackStack("food")
        parentFragmentManager.clearBackStack("waterCons")
        parentFragmentManager.clearBackStack("BMI")
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(backStackName)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}