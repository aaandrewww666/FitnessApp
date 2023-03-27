package com.example.fitnessapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentMainBinding
import com.example.fitnessapp.ui.data.WeightData

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)

        parentFragmentManager.commit {
            replace(R.id.fragment_container, HomeFragment())
            disallowAddToBackStack()
        }

        binding.bottomNavigationView.onTabSelected = {
            when (it.id) {
                R.id.settings -> loadFragment(SettingsFragment())
                R.id.home -> loadFragment(HomeFragment())
                R.id.stats -> loadFragment(StatisticsFragment())
            }
        }

        binding.bottomNavigationView.onTabReselected = {
            when (it.id) {
                R.id.settings -> loadFragment(SettingsFragment())
                R.id.home -> loadFragment(HomeFragment())
                R.id.stats -> loadFragment(StatisticsFragment())
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private  fun loadFragment(fragment: Fragment){
        for (i in 0..parentFragmentManager.backStackEntryCount) {
            parentFragmentManager.popBackStack()
        }
        parentFragmentManager.commit {
            replace(R.id.fragment_container,fragment)
            disallowAddToBackStack()
        }
    }
}