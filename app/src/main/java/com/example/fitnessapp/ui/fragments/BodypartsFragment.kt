package com.example.fitnessapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentBodypartsBinding
import com.example.fitnessapp.ui.data.adapters.BodypartsAdapter
import java.util.*
import kotlin.collections.ArrayList

class BodypartsFragment : Fragment() {

    private var _binding: FragmentBodypartsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBodypartsBinding.inflate(inflater)
        val itemList : ArrayList<String>
        Log.d("sss", Locale.getDefault().country)
        if(Locale.getDefault().country != "RU") {
            itemList = ArrayList(
                listOf(
                    "back",
                    "cardio",
                    "chest",
                    "lower arms",
                    "lower legs",
                    "neck",
                    "shoulders",
                    "upper arms",
                    "upper legs",
                    "waist"
                )
            )
        } else {
            itemList = ArrayList(
                listOf(
                    "спина",
                    "кардио",
                    "грудь",
                    "нижняя часть рук",
                    "голени",
                    "шея",
                    "плечи",
                    "верхняя часть рук",
                    "верхняя часть ног",
                    "талия"
                )
            )
        }

        binding.rvBodyparts.adapter = BodypartsAdapter(itemList, object : BodypartsAdapter.OnItemClickListener {
            override fun clickListener(item: String) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ExercisesFragment(item))
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