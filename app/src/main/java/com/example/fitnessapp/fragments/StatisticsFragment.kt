package com.example.fitnessapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.api.results.WeightResult
import com.example.fitnessapp.data.adapters.WeightAdapter
import com.example.fitnessapp.databinding.FragmentStatisticsBinding
import com.example.fitnessapp.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<StatisticsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            if (it.checking) {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                binding.addWeightBtn.visibility = Button.INVISIBLE
                binding.rvWeightData.visibility = RecyclerView.INVISIBLE
                binding.addWeightBtn.isEnabled = false
                binding.notFoundImg.visibility = ImageView.INVISIBLE
                binding.linearLayout.visibility = LinearLayout.INVISIBLE
            } else {
                binding.progressBar.visibility = ProgressBar.INVISIBLE
                binding.notFoundImg.visibility = ImageView.INVISIBLE
                binding.addWeightBtn.visibility = Button.VISIBLE
                binding.rvWeightData.visibility = RecyclerView.VISIBLE
                binding.addWeightBtn.isEnabled = true
                binding.linearLayout.visibility = LinearLayout.VISIBLE
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                viewModel.changeState(true)
            }
            viewModel.getUserWeights()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.weight.collect { result ->
                when (result) {
                    is WeightResult.Successful -> {
                        withContext(Dispatchers.Main) {
                            val entries = ArrayList<Entry>()
                            val xLabels = ArrayList<String>()
                            var counter = 0.0f
                            result.data.userWeights.forEach { elem ->
                                xLabels.add(elem.date)
                                entries.add(Entry(counter, elem.userWeight.toFloat()))
                                counter++
                            }
                            val lineDataset = LineDataSet(entries, "Вес")
                            lineDataset.color = Color.WHITE
                            lineDataset.setCircleColor(Color.WHITE)
                            lineDataset.valueTextColor = Color.WHITE
                            lineDataset.highLightColor = Color.RED

                            val data = LineData(lineDataset)
                            data.setValueTextColor(Color.WHITE)
                            data.isHighlightEnabled = true

                            val chart = binding.chart
                            chart.data = data
                            chart.setScaleEnabled(true)
                            chart.xAxis.spaceMin = 1f
                            chart.axisRight.isEnabled = false
                            chart.legend.isEnabled = false
                            chart.xAxis.textColor = Color.WHITE
                            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                            chart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
                            chart.axisLeft.textColor = Color.WHITE
                            chart.description.isEnabled = false

                            binding.rvWeightData.adapter = WeightAdapter(result.data.userWeights)
                            binding.rvWeightData.layoutManager =
                                LinearLayoutManager(binding.root.context)
                            viewModel.changeState(false)
                        }
                    }
                    is WeightResult.Error -> {
                        withContext(Dispatchers.Main) {
                            showSnackbar(binding.root, result.errorMessage.toString())
                        }
                    }
                    is WeightResult.NotFound -> {
                        withContext(Dispatchers.Main) {
                            binding.progressBar.visibility = ProgressBar.INVISIBLE
                            binding.notFoundImg.visibility = ImageView.VISIBLE
                            binding.addWeightBtn.visibility = Button.VISIBLE
                            binding.addWeightBtn.isEnabled = true
                            showSnackbar(binding.root, "Данных не найдено")
                        }
                    }
                    else -> {
                        withContext(Dispatchers.Main) {
                            showSnackbar(binding.root, "Что-то не так...")
                        }
                    }
                }
            }
        }

        binding.addWeightBtn.setOnClickListener{
            val bottomDialog = BottomDialogFragment()
            bottomDialog.show(childFragmentManager,"bottomSheet")
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