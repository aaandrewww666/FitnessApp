package com.example.fitnessapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.fitnessapp.R
import com.example.fitnessapp.ui.firebase.FireAuth

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button_exit_from_app).setOnClickListener {
            val pref = activity?.getPreferences(Context.MODE_PRIVATE)!!
            pref.edit().clear().apply()
            FireAuth.firebaseAuth.signOut()
            requireActivity().finish()
        }
    }
}