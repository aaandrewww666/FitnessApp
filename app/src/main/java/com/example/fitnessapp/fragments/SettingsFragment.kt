package com.example.fitnessapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fitnessapp.R
import com.example.fitnessapp.api.responses.UserDataResponse
import com.example.fitnessapp.api.results.DataResult
import com.example.fitnessapp.databinding.FragmentBMIBinding
import com.example.fitnessapp.databinding.FragmentSettingsBinding
import com.example.fitnessapp.databinding.FragmentStatisticsBinding
import com.example.fitnessapp.viewmodels.BMIViewModel
import com.example.fitnessapp.viewmodels.SettingsViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            if (it.checking) {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                binding.linearLayoutInfo.visibility = LinearLayout.INVISIBLE
                binding.textInputLayoutAge.visibility = TextInputLayout.INVISIBLE
                binding.buttonExitFromApp.visibility = Button.INVISIBLE
                binding.buttonExitFromApp.isEnabled = false
                binding.buttonUpdateAge.isEnabled = false
                binding.buttonUpdateAge.visibility = Button.INVISIBLE
            } else {
                binding.progressBar.visibility = ProgressBar.INVISIBLE
                binding.textInputLayoutAge.visibility = TextInputLayout.VISIBLE
                binding.linearLayoutInfo.visibility = LinearLayout.VISIBLE
                binding.buttonExitFromApp.visibility = Button.VISIBLE
                binding.buttonExitFromApp.isEnabled = true
                binding.buttonUpdateAge.isEnabled = true
                binding.buttonUpdateAge.visibility = Button.VISIBLE
            }
        }

        updateAgeView(view)

        binding.buttonUpdateAge.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val age = binding.ageIET.text.toString().toInt()
                if (age in 10..99) {
                    withContext(Dispatchers.Main) {
                        viewModel.changeState(true)
                    }
                    when (val gettingDataResult = viewModel.getUserData().single()) {
                        is DataResult.Successful -> {
                                when (val updatingDataResult = viewModel.updateUserData(
                                    age,
                                    gettingDataResult.data!!.gender,
                                    gettingDataResult.data.height
                                ).single()) {
                                    is DataResult.Error -> {
                                        updateAgeView(view)
                                        withContext(Dispatchers.Main) {
                                            binding.ageIET.setText("")
                                            showSnackbar(
                                                view,
                                                updatingDataResult.errorMessage.toString()
                                            )
                                        }
                                    }
                                    is DataResult.Successful -> {
                                        updateAgeView(view)
                                        withContext(Dispatchers.Main) {
                                            binding.ageIET.setText("")
                                            showSnackbar(view, "Успешно")
                                        }
                                    }
                                    else -> {
                                        withContext(Dispatchers.Main) {
                                            showSnackbar(
                                                view,
                                                "Что-то не так...",
                                            )
                                            binding.buttonExitFromApp.visibility = Button.VISIBLE
                                            binding.buttonExitFromApp.isEnabled = true
                                            binding.progressBar.visibility = ProgressBar.INVISIBLE
                                        }
                                    }
                                }
                        }
                        is DataResult.Error -> {
                            withContext(Dispatchers.Main) {
                                showSnackbar(
                                    view,
                                    gettingDataResult.errorMessage.toString()
                                )
                                binding.buttonExitFromApp.visibility = Button.VISIBLE
                                binding.buttonExitFromApp.isEnabled = true
                                binding.progressBar.visibility = ProgressBar.INVISIBLE
                            }

                        }
                        else -> {
                            withContext(Dispatchers.Main) {
                                showSnackbar(
                                    view,
                                    "Что-то не так...",
                                )

                                binding.buttonExitFromApp.visibility = Button.VISIBLE
                                binding.buttonExitFromApp.isEnabled = true
                                binding.progressBar.visibility = ProgressBar.INVISIBLE
                            }
                        }
                    }
                } else {
                    showSnackbar(binding.root, "Недопустимый возраст")
                }
            }

            binding.buttonExitFromApp.setOnClickListener {
                val sharedPrefs =
                    context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val editor = sharedPrefs!!.edit()
                editor.clear()
                editor.apply()
                requireActivity().finish()
            }
        }
    }

    private fun updateAgeView(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                viewModel.changeState(true)
            }
            when (val gettingDataResult = viewModel.getUserData().single()) {
                is DataResult.Successful -> {
                    withContext(Dispatchers.Main) {
                        binding.textViewInfoShow.text = gettingDataResult.data!!.age.toString()
                        viewModel.changeState(false)
                    }
                }
                is DataResult.Error -> {
                    withContext(Dispatchers.Main) {
                        showSnackbar(view, gettingDataResult.errorMessage.toString())
                        binding.buttonExitFromApp.visibility = Button.VISIBLE
                        binding.buttonExitFromApp.isEnabled = true
                        binding.progressBar.visibility = ProgressBar.INVISIBLE
                    }

                }
                else -> {
                    withContext(Dispatchers.Main) {
                        showSnackbar(view, "Что-то не так...")
                        binding.buttonExitFromApp.visibility = Button.VISIBLE
                        binding.buttonExitFromApp.isEnabled = true
                        binding.progressBar.visibility = ProgressBar.INVISIBLE
                    }
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