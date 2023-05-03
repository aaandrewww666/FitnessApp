package com.example.fitnessapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentExercisesBinding
import com.example.fitnessapp.data.adapters.ExercisesAdapter
import com.example.fitnessapp.data.models.Exercises
import com.example.fitnessapp.data.models.PlanarClassification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ExercisesFragment(private val classification : String) : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesBinding.inflate(inflater)
        val jsonString by lazy {
            requireContext().resources.openRawResource(R.raw.exercises).bufferedReader().use { it.readText() }
        }
        val listPlanarClassificationType = object : TypeToken<List<Exercises>>() {}.type
        val listPlanarClassification: List<Exercises> = Gson().fromJson(
            jsonString,
            listPlanarClassificationType
        )
        val exercises : List<Exercises> =
            listPlanarClassification.filter {item -> item.classification == classification }
        binding.recyclerViewExercises.adapter = ExercisesAdapter(exercises)

        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(binding.root.context)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}