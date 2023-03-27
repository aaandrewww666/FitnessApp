package com.example.fitnessapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.fitnessapp.databinding.FragmentWaterConsumptionBinding
import com.example.fitnessapp.ui.model.ViewModel
import kotlinx.coroutines.coroutineScope

class WaterConsumptionFragment : Fragment() {

    private val dataModel : ViewModel by activityViewModels()
    private var _binding: FragmentWaterConsumptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaterConsumptionBinding.inflate(inflater)
        dataModel.getUserWeight.observe(activity as LifecycleOwner) {
            val wd = it
            var averageWeight = 0.0
            Log.d("sss",wd.toString())
            wd.forEach { elem ->
                averageWeight += elem.weight
            }
            averageWeight /= wd.size

            Log.d("sss",averageWeight.toString())
            binding.litresNumber.text =
                "%.3f".format(((averageWeight * 2.20462 * 15)/1000).toFloat()).replace(",",".")
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}