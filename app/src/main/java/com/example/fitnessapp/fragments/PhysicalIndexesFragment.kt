package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.api.results.DataResult
import com.example.fitnessapp.api.results.WeightResult
import com.example.fitnessapp.databinding.FragmentPhysicalIndexesBinding
import com.example.fitnessapp.viewmodels.PhysicalIndexesViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class PhysicalIndexesFragment : Fragment() {

    private var _binding: FragmentPhysicalIndexesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PhysicalIndexesViewModel>()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhysicalIndexesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            if (it.checking) {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                binding.continueButton.isEnabled = false
                binding.continueButton.visibility = Button.INVISIBLE
                binding.linearLayoutAge.visibility = LinearLayout.INVISIBLE
                binding.linearLayoutHeight.visibility = LinearLayout.INVISIBLE
                binding.linearLayoutWeight.visibility = LinearLayout.INVISIBLE
                binding.linearLayoutGender.visibility = LinearLayout.INVISIBLE
            } else {
                binding.progressBar.visibility = ProgressBar.INVISIBLE
                binding.continueButton.isEnabled = true
                binding.continueButton.visibility = Button.VISIBLE
                binding.linearLayoutAge.visibility = LinearLayout.VISIBLE
                binding.linearLayoutHeight.visibility = LinearLayout.VISIBLE
                binding.linearLayoutWeight.visibility = LinearLayout.VISIBLE
                binding.linearLayoutGender.visibility = LinearLayout.VISIBLE
            }
        }

        viewModel.ageValue.observe(viewLifecycleOwner) { age ->
            binding.ageET.setText(age.toString())
        }

        viewModel.weightValue.observe(viewLifecycleOwner) { weight ->
            binding.weightET.setText(weight.toString())
        }

        viewModel.heightValue.observe(viewLifecycleOwner) { height ->
            binding.heightET.setText(height.toString())
        }

        binding.buttonAgeMinus.setOnClickListener {
            if(binding.ageET.text.isNotEmpty()) {
                val age = binding.ageET.text.toString().toInt() - 1
                if(age in 10..99) {
                    viewModel.updateAge(age)
                } else {
                    showSnackbar(binding.root, "Недопустимый возраст")
                }
            } else {
                showSnackbar(binding.root, "Недопустимый формат")
            }

        }

        binding.buttonAgePlus.setOnClickListener {
            if(binding.ageET.text.isNotEmpty()) {
                val age = binding.ageET.text.toString().toInt() + 1
                if(age in 10..99) {
                    viewModel.updateAge(age)
                } else {
                    showSnackbar(binding.root, "Недопустимый возраст")
                }
            } else {
                showSnackbar(binding.root, "Недопустимый формат")
            }
        }

        binding.buttonWeightMinus.setOnClickListener {
            if(binding.weightET.text.isNotEmpty() && binding.weightETFraction.text.isNotEmpty()) {
                val weight = binding.weightET.text.toString().toInt() + binding.weightETFraction.text.toString().toDouble()/10 - 1
                if(weight in 20.0..200.0) {
                    viewModel.updateWeight(binding.weightET.text.toString().toInt() - 1)
                } else {
                    showSnackbar(binding.root, "Недопустимый вес")
                }
            } else {
                showSnackbar(binding.root, "Недопустимый формат")
            }
        }

        binding.buttonWeightPlus.setOnClickListener {
            if(binding.weightET.text.isNotEmpty() && binding.weightETFraction.text.isNotEmpty()) {
                val weight = binding.weightET.text.toString().toInt() + binding.weightETFraction.text.toString().toDouble()/10 + 1
                if(weight in 20.0..200.0) {
                    viewModel.updateWeight(binding.weightET.text.toString().toInt() + 1)
                } else {
                    showSnackbar(binding.root, "Недопустимый вес")
                }
            } else {
                showSnackbar(binding.root, "Недопустимый формат")
            }
        }

        binding.heightSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.updateHeight(value.toInt())
        }

        binding.continueButton.setOnClickListener {
            viewModel.changeState(true)
            if (
                binding.ageET.text.isNotEmpty() &&
                binding.weightET.text.isNotEmpty() &&
                binding.heightET.text.isNotEmpty()
            ) {
                val age = binding.ageET.text.toString().toInt()
                val height = binding.heightET.text.toString().toDouble()
                var gender = 0
                when (binding.radioGroupGender.checkedRadioButtonId) {
                    R.id.female_radiobutton -> gender = 1
                }
                val weight = binding.weightET.text.toString().toDouble()
                if (
                    age in 10..99 &&
                    height in 100.0..250.0 &&
                    weight in 20.0..200.0
                ) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        when (val addingDataResult = viewModel.addUserData(age, gender, height).single()) {
                            is DataResult.Error -> {
                                withContext(Dispatchers.Main) {
                                    viewModel.changeState(false)
                                }
                                showSnackbar(binding.root, addingDataResult.errorMessage.toString())
                            }
                            DataResult.Posted -> {
                                when(val addingWeightResult = viewModel.addUserWeight(weight, LocalDate.now().toString()).single()) {
                                    is WeightResult.Posted -> {
                                        withContext(Dispatchers.Main) {
                                            navController.navigate(R.id.action_physicalIndexesFragment_to_mainFragment)
                                        }
                                    }
                                    is WeightResult.Error -> {
                                        withContext(Dispatchers.Main) {
                                            viewModel.changeState(false)
                                        }
                                        showSnackbar(binding.root, addingWeightResult.errorMessage.toString())
                                    }
                                    else -> {
                                        withContext(Dispatchers.Main) {
                                            viewModel.changeState(false)
                                        }
                                        showSnackbar(binding.root, "Что-то не так...")
                                    }
                                }
                            }
                            else -> {
                                withContext(Dispatchers.Main) {
                                    viewModel.changeState(false)
                                }
                                showSnackbar(binding.root, "Что-то не так...")
                            }
                        }

                    }
                } else {
                    viewModel.changeState(false)
                    if(Locale.getDefault().country != "RU") {
                        showSnackbar(binding.root, "Write correct data")
                    } else {
                        showSnackbar(binding.root, "Введите корректные данные")
                    }
                }
            } else {
                viewModel.changeState(false)
                if(Locale.getDefault().country != "RU") {
                    showSnackbar(binding.root, "Fill the fields")
                } else {
                    showSnackbar(binding.root, "Заполните поля")
                }
            }
        }
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