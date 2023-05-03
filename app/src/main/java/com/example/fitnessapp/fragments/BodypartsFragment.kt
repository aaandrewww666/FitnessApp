package com.example.fitnessapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentBodypartsBinding
import com.example.fitnessapp.data.adapters.ClassificationAdapter
import com.example.fitnessapp.data.models.PlanarClassification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BodypartsFragment : Fragment() {

    private var _binding: FragmentBodypartsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBodypartsBinding.inflate(inflater)

        val jsonString by lazy {
            requireContext().resources.openRawResource(R.raw.planar_training).bufferedReader().use { it.readText() }
        }
        val listPlanarClassificationType = object : TypeToken<List<PlanarClassification>>() {}.type
        val bodyparts: List<PlanarClassification> = Gson().fromJson(
            jsonString,
            listPlanarClassificationType
        )
        binding.rvBodyparts.adapter = ClassificationAdapter(bodyparts, object : ClassificationAdapter.OnItemClickListener {
            override fun clickListener(item: PlanarClassification) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ExercisesFragment(item.name))
                    .addToBackStack("exercises")
                    .commit()
            }

        })
        binding.rvBodyparts.layoutManager = LinearLayoutManager(binding.root.context)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}