package com.example.fitnessapp.ui.identification.users.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentAuthorizationBinding
import com.example.fitnessapp.ui.firebase.FireAuth.firebaseAuth
import com.example.fitnessapp.ui.firebase.FireStore
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.internal.api.FirebaseNoSignedInUserException

class AuthenticationFragment : Fragment() {
    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        if (pref.getString("save_login", null) != null
            && pref.getString("save_pass", null) != null
        ) {
            firebaseAuth.signInWithEmailAndPassword(
                pref.getString("save_login", "test")!!,
                pref.getString("save_pass", "test")!!
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    FireStore.firebaseStore.collection("usersEntries")
                        .document(firebaseAuth.uid.toString())
                        .get()
                        .addOnSuccessListener { result ->
                            Log.d("iii", result.get(firebaseAuth.uid.toString()).toString())
                            if (result.get(firebaseAuth.uid.toString()).toString() == "0") {
                                lifecycleScope.launchWhenResumed {
                                    navController.navigate(R.id.action_authorizationFragment_to_physicalIndexesFragment)
                                }
                            } else {
                                lifecycleScope.launchWhenResumed {
                                    navController.navigate(R.id.action_authorizationFragment_to_mainFragment)
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.w("iii", "Error getting documents.", exception)
                        }
                } else {
                    try {
                        throw it.exception!!
                    }  catch (e: FirebaseAuthInvalidUserException) {
                        Snackbar.make(
                            binding.root,
                            "Пользователя с такой почтой не существует",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAnimationMode(ANIMATION_MODE_SLIDE)
                            .show()
                    } catch (e : FirebaseAuthInvalidCredentialsException) {
                        Snackbar.make(
                            binding.root,
                            "Пароль неверный",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAnimationMode(ANIMATION_MODE_SLIDE)
                            .show()
                    }
                }
            }
        } else {
            binding.regBtn.setOnClickListener { navController.navigate(R.id.registrationFragment) }

            binding.enterBtn.setOnClickListener {
                val login = binding.emailIET.text.toString()
                val pass = binding.passwordIET.text.toString()
                if (
                    login.isNotEmpty() and
                    pass.isNotEmpty()
                ) {
                    if(isEmailValid(login)) {
                    firebaseAuth.signInWithEmailAndPassword(login, pass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                setPrefs(login, pass)
                                FireStore.firebaseStore.collection("usersEntries")
                                    .document(firebaseAuth.uid.toString())
                                    .get()
                                    .addOnSuccessListener { result ->
                                        Log.d("iii", result.get(firebaseAuth.uid.toString()).toString())
                                        if (result.get(firebaseAuth.uid.toString()).toString() == "0") {
                                            navController.navigate(R.id.action_authorizationFragment_to_physicalIndexesFragment)
                                        } else {
                                            navController.navigate(R.id.action_authorizationFragment_to_mainFragment)
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Snackbar.make(
                                            binding.root,
                                            "Непредвиденная ошибка",
                                            Snackbar.LENGTH_SHORT
                                        )
                                            .setAnimationMode(ANIMATION_MODE_SLIDE)
                                            .show()
                                    }
                            }else {
                                try {
                                    throw it.exception!!
                                }  catch (e: FirebaseAuthInvalidUserException) {
                                    Snackbar.make(
                                        binding.root,
                                        "Пользователя с такой почтой не существует",
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .setAnimationMode(ANIMATION_MODE_SLIDE)
                                        .show()
                                } catch (e : FirebaseAuthInvalidCredentialsException) {
                                    Snackbar.make(
                                        binding.root,
                                        "Пароль неверный",
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
                    Snackbar.make(
                        binding.root,
                        "Заполните поля",
                        Snackbar.LENGTH_SHORT
                    )
                        .setAnimationMode(ANIMATION_MODE_SLIDE)
                        .show()
                }
            }

        }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setPrefs(login: String, password: String) {
        val pref = activity?.getPreferences(Context.MODE_PRIVATE)
        with(pref!!.edit()) {
            putString("save_login", login)
            putString("save_pass", password)
            apply()
        }
    }
}