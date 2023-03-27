package com.example.fitnessapp.ui.identification.users.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentRegistrationBinding
import com.example.fitnessapp.ui.firebase.FireAuth
import com.example.fitnessapp.ui.firebase.FireStore
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(layoutInflater)

        val firebaseAuth = FireAuth.firebaseAuth

        binding.regBtn.setOnClickListener {
            val email = binding.emailIET.text.toString()
            val pass = binding.passwordIET.text.toString()
            val confirmPass = binding.confirmPasswordIET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    if(isEmailValid(email)) {
                        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val db = FireStore.firebaseStore
                                    val hmp = HashMap<String, Int>()
                                    hmp[firebaseAuth.uid.toString()] = 0
                                    db.collection("usersEntries")
                                        .document(firebaseAuth.uid.toString())
                                        .set(hmp)
                                        .addOnSuccessListener {
                                            Snackbar.make(
                                                binding.root,
                                                "Успешная регистрация",
                                                Snackbar.LENGTH_SHORT
                                            )
                                                .setAnimationMode(ANIMATION_MODE_SLIDE)
                                                .show()
                                            findNavController().navigate(R.id.authorizationFragment)
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w("iii", "Smth went wrong" + e.message)
                                        }
                                } else {
                                    try {
                                        throw it.exception!!
                                    } catch (e: FirebaseAuthWeakPasswordException) {
                                        Snackbar.make(
                                            binding.root,
                                            "Слабый пароль",
                                            Snackbar.LENGTH_SHORT
                                        )
                                            .setAnimationMode(ANIMATION_MODE_SLIDE)
                                            .show()
                                    }  catch (e: FirebaseAuthUserCollisionException) {
                                        Snackbar.make(
                                            binding.root,
                                            "Пользователь с такой почтой уже существует",
                                            Snackbar.LENGTH_SHORT
                                        )
                                            .setAnimationMode(ANIMATION_MODE_SLIDE)
                                            .show()
                                    }
                                }
                            }
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Проверьте почту",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAnimationMode(ANIMATION_MODE_SLIDE)
                            .show()
                    }
                } else {
                    Snackbar.make(binding.root,"Пароли не совпадают", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(ANIMATION_MODE_SLIDE)
                        .show()
                }
            } else {
                Snackbar.make(binding.root,"Заполните поля",Snackbar.LENGTH_SHORT)
                    .setAnimationMode(ANIMATION_MODE_SLIDE)
                    .show()
            }
        }
        return binding.root
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}