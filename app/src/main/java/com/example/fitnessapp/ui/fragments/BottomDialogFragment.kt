package com.example.fitnessapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.example.fitnessapp.R
import com.example.fitnessapp.ui.viewmodels.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomDialogFragment : BottomSheetDialogFragment() {

    private val dataModel : ViewModel by activityViewModels()

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

            if(editTextWeight.text.toString().toInt() in 21..199) {
            val day: Int = datePicker.dayOfMonth
            val month: Int = datePicker.month + 1
            val year: Int = datePicker.year
            dataModel.updateUserWeight(
                "$day.$month.$year",
                editTextWeight.text.toString().toInt()
            )
            Toast.makeText(view.context, "Успешно!", Toast.LENGTH_SHORT).show()

            dismiss()
        } else {
                Toast.makeText(view.context, "Введите корректные данные", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }
}