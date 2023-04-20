package com.example.fitnessapp.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.fitnessapp.databinding.FragmentExerciseBinding
import com.example.fitnessapp.data.Exercises

class ExerciseFragment(var item : Exercises) : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater)
        binding.exerciseName.text = item.name
        binding.targetTV.text = item.target
        binding.equipmentTV.text = item.equipment

        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.setColorSchemeColors(Color.BLACK)
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 50f
        circularProgressDrawable.start()

        Glide
            .with(requireContext())
            .load(item.gifUrl)
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .into(binding.imageViewExercise)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}