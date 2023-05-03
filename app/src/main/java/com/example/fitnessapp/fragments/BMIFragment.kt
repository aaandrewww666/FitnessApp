package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.api.results.DataResult
import com.example.fitnessapp.api.results.WeightResult
import com.example.fitnessapp.data.adapters.BMIAdapter
import com.example.fitnessapp.databinding.FragmentBMIBinding
import com.example.fitnessapp.viewmodels.BMIViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class BMIFragment : Fragment() {
    private var _binding: FragmentBMIBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<BMIViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBMIBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            if (it.checking) {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                binding.scrollViewBMI.visibility = ScrollView.INVISIBLE

            } else {
                binding.progressBar.visibility = ProgressBar.INVISIBLE
                binding.scrollViewBMI.visibility = ScrollView.VISIBLE
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                viewModel.changeState(true)
            }
            when (val gettingDataResult = viewModel.getUserData().single()) {
                is DataResult.Successful -> {
                    when(val gettingWeightResult = viewModel.getUserWeight().single()) {
                        is WeightResult.Successful -> {
                            val weight = gettingWeightResult.data.userWeights.last().userWeight
                            val height = gettingDataResult.data!!.height
                            val age = gettingDataResult.data.age
                            val bmiResult =  weight / (height * height) * 10000
                            val gender: String = if(gettingDataResult.data.gender == 0)  "Мужской" else "Женский"
                            val bmrResult = if(gender == "Мужской") 10 * weight + 6.25 * height - 5 * age + 5 else  10 * weight + 6.25 * height - 5 * age - 161
                            val bmiResultDescription = if (bmiResult < 18.5) {
                                "У Вас недостаточный вес"
                            } else if (bmiResult >= 18.5 && bmiResult < 25.0) {
                                "Ваш вес в норме"
                            } else if (bmiResult >= 25 && bmiResult < 30) {
                                "У Вас лишний вес"
                            } else if (bmiResult >= 30 && bmiResult < 40) {
                                "У Вас ожирение"
                            } else {
                                "У Вас болезненное ожирение"
                            }
                            withContext(Dispatchers.Main) {
                                binding.recyclerViewUserInfo.adapter = BMIAdapter(
                                    ArrayList(
                                        listOf(
                                            getString(R.string.added_height),
                                            getString(R.string.last_added_weight),
                                            getString(R.string.age),
                                            getString(R.string.gender)
                                        )
                                    ),
                                    ArrayList(
                                        listOf(
                                            height.toInt().toString(),
                                            weight.toString(),
                                            age.toString(),
                                            gender
                                        )
                                    )
                                )
                                binding.textViewBMIDescriptionShow.text = bmiResultDescription
                                binding.textViewBMIShow.text = String.format("%.2f", bmiResult)
                                binding.textViewBMRShow.text = bmrResult.toInt().toString() + " ккал"
                                viewModel.changeState(false)
                            }
                        }
                        is WeightResult.Error -> {
                            withContext(Dispatchers.Main) {
                                viewModel.changeState(false)
                                showSnackbar(binding.root, gettingWeightResult.errorMessage.toString())
                            }
                        }
                        is WeightResult.NotFound -> {
                            withContext(Dispatchers.Main) {
                                binding.progressBar.visibility = ProgressBar.INVISIBLE
                                binding.notFoundImg.visibility = ImageView.VISIBLE
                                showSnackbar(binding.root,"Данных не найдено")
                            }
                        }
                        else -> {
                            withContext(Dispatchers.Main) {
                                viewModel.changeState(false)
                                showSnackbar(binding.root,"Что-то не так...")
                            }
                        }
                    }
                }
                is DataResult.Error -> {
                    withContext(Dispatchers.Main) {
                        viewModel.changeState(false)
                        showSnackbar(binding.root, gettingDataResult.errorMessage.toString())
                    }
                }
                is DataResult.NotFound -> {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = ProgressBar.INVISIBLE
                        binding.notFoundImg.visibility = ImageView.VISIBLE
                        showSnackbar(binding.root,"Данных не найдено")
                    }
                }
                else -> {
                    withContext(Dispatchers.Main) {
                        viewModel.changeState(false)
                        showSnackbar(binding.root,"Что-то не так...")
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