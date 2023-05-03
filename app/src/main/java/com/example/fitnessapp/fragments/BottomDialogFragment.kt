package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fitnessapp.R
import com.example.fitnessapp.api.results.WeightResult
import com.example.fitnessapp.viewmodels.StatisticsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@AndroidEntryPoint
class BottomDialogFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<StatisticsViewModel>( ownerProducer = { requireParentFragment() } )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editTextWeight = view.findViewById<EditText>(R.id.dialogWeightET)
        val editTextWeightFraction = view.findViewById<EditText>(R.id.dialogWeightFractionET)
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        view.findViewById<Button>(R.id.button_add_weight).setOnClickListener {
            val weight = editTextWeight.text.toString().toDouble() + editTextWeightFraction.text.toString().toDouble()/10
            if(weight in 21.0..199.0) {
                val day: Int = datePicker.dayOfMonth
                val month: Int = datePicker.month + 1
                val year: Int = datePicker.year
                view.findViewById<Button>(R.id.button_add_weight).isEnabled = false
                lifecycleScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main){
                        viewModel.changeState(true)
                    }
                    when(val result = viewModel.addUserWeight(weight,LocalDate.of(year,month,day).toString()).single()){
                        is WeightResult.Error -> {
                            viewModel.getUserWeights()
                            withContext(Dispatchers.Main){
                                showSnackbar(view, result.errorMessage.toString())
                                viewModel.changeState(false)
                                view.findViewById<Button>(R.id.button_add_weight).isEnabled = true
                            }
                        }
                        is WeightResult.Posted -> {
                            viewModel.getUserWeights()
                            withContext(Dispatchers.Main){
                                viewModel.changeState(false)
                                view.findViewById<Button>(R.id.button_add_weight).isEnabled = true
                                dismiss()
                            }
                        }
                        else -> {
                            viewModel.getUserWeights()
                            withContext(Dispatchers.Main){
                                showSnackbar(view, result.toString())
                                viewModel.changeState(false)
                                view.findViewById<Button>(R.id.button_add_weight).isEnabled = true
                            }

                        }
                    }
                }

        } else {
            showSnackbar(view,"Введите корректные данные" )
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
}