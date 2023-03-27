package com.example.fitnessapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentStatisticsBinding
import com.example.fitnessapp.ui.data.adapters.WeightAdapter
import com.example.fitnessapp.ui.viewmodels.ViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlin.collections.ArrayList

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val dataModel : ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater)
        dataModel.getUserWeight.observe(activity as LifecycleOwner) { weightList ->
            val chart = binding.chart
            val entries = ArrayList<Entry>()
            val xValues = ArrayList<String>()
            var counter = 0.0f
            weightList.forEach { elem ->
                xValues.add(elem.data.toString())
                entries.add(Entry(counter, elem.weight.toFloat()))
                counter++
            }
            val lineDataset = LineDataSet(entries, "Weights")
            lineDataset.color = R.color.blue

            val data = LineData(lineDataset)
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.animateY(1000)
            chart.description.text = ""
            chart.data = data
            chart.xAxis.granularity = 1.0f
            chart.rendererRightYAxis
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
            binding.rvWeightData.adapter = WeightAdapter(weightList)
            binding.rvWeightData.layoutManager = LinearLayoutManager(binding.root.context)

            binding.addWeightBtn.setOnClickListener{
                val bottomDialog = BottomDialogFragment()
                bottomDialog.show(childFragmentManager,"bottomSheet")
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}