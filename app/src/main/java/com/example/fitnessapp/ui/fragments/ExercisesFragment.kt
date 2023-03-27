package com.example.fitnessapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentExercisesBinding
import com.example.fitnessapp.ui.data.Exercises
import com.example.fitnessapp.ui.data.adapters.ExercisesAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ExercisesFragment(private val bodyPart : String) : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesBinding.inflate(inflater)
        val exercises : List<Exercises> = if(Locale.getDefault().country != "RU") {
            getExercises(R.raw.exersises)
                .filter { exercise -> exercise.bodyPart == bodyPart }
        } else {
            getExercises(R.raw.exersises_ru)
                .filter { exercise -> exercise.bodyPart == bodyPart }
        }
        binding.rvExercises.adapter = ExercisesAdapter(exercises, object : ExercisesAdapter.OnItemClickListener {
            override fun clickListener(item: Exercises) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ExerciseFragment(item))
                    .addToBackStack("exercises")
                    .commit()
            }
        })
        binding.rvExercises.layoutManager = LinearLayoutManager(binding.root.context)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getExercises(resource : Int): List<Exercises> {
        val jsonString by lazy {
            requireContext().resources.openRawResource(resource).bufferedReader().use { it.readText() }
        }
        val listCountryType = object : TypeToken<List<Exercises>>() {}.type
        return Gson().fromJson(jsonString, listCountryType)
    }
}