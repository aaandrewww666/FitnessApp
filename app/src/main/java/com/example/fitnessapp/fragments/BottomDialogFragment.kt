package com.example.fitnessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.fitnessapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BottomDialogFragment : BottomSheetDialogFragment() {

    //private val dataModel : ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editTextWeight = view.findViewById<EditText>(R.id.weight_edittext)
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        view.findViewById<Button>(R.id.button_add_weight).setOnClickListener {
            val weight = editTextWeight.text.toString().toDouble()
            if(weight in 21.0..199.0) {
            val day: Int = datePicker.dayOfMonth
            val month: Int = datePicker.month + 1
            val year: Int = datePicker.year
            /*dataModel.updateUserWeight(
                "$day.$month.$year",
                weight
            )*/
            Toast.makeText(view.context, "Успешно!", Toast.LENGTH_SHORT).show()

            dismiss()
        } else {
                Toast.makeText(view.context, "Введите корректные данные", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }
}