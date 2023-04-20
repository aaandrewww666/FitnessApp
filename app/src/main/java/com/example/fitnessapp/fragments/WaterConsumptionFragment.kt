package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fitnessapp.api.results.WeightResult
import com.example.fitnessapp.databinding.FragmentWaterConsumptionBinding
import com.example.fitnessapp.viewmodels.WaterConsumptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class WaterConsumptionFragment : Fragment() {

    private val viewModel by viewModels<WaterConsumptionViewModel>()
    private var _binding: FragmentWaterConsumptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaterConsumptionBinding.inflate(inflater)
        lifecycleScope.launch(Dispatchers.IO) {
            when(val weights = viewModel.getUserWeights().single()) {
                is WeightResult.NotFound -> TODO()
                is WeightResult.Successful -> {
                    var averageWeight = 0.0
                    weights.data.userWeights.forEach { elem ->
                        averageWeight += elem.userWeight
                    }
                    averageWeight /= weights.data.userWeights.size
                    withContext(Dispatchers.Main) {
                        viewModel.changeState(false)
                        binding.litresNumber.text =
                            "%.3f".format(((averageWeight * 2.20462 * 15) / 1000).toFloat())
                                .replace(",", ".")
                    }
                }
                else -> {

                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.changeState(true)
        viewModel.state.observe(viewLifecycleOwner) {
            if (it.checking) {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                binding.personShouldDrinkTV.visibility = TextView.INVISIBLE
                binding.waterImage.visibility = ImageView.INVISIBLE
                binding.ofFluidsTV.visibility = TextView.INVISIBLE
                binding.showLitresLL.visibility = LinearLayout.INVISIBLE
            } else {
                binding.progressBar.visibility = ProgressBar.GONE
                binding.personShouldDrinkTV.visibility = TextView.VISIBLE
                binding.waterImage.visibility = ImageView.VISIBLE
                binding.ofFluidsTV.visibility = TextView.VISIBLE
                binding.showLitresLL.visibility = LinearLayout.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}