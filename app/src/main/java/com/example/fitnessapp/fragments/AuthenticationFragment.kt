package com.example.fitnessapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.api.results.AuthResult
import com.example.fitnessapp.api.results.DataResult
import com.example.fitnessapp.databinding.FragmentAuthorizationBinding
import com.example.fitnessapp.viewmodels.AuthViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AuthenticationFragment : Fragment() {
    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(layoutInflater)
        lifecycleScope.launch(Dispatchers.IO) {
            when(viewModel.authenticate().single()) {
                is AuthResult.Authorized -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        when(viewModel.checkUserData().single()) {
                            is DataResult.Successful -> {
                                withContext(Dispatchers.Main) {
                                    navController.navigate(R.id.action_authorizationFragment_to_mainFragment)
                                    viewModel.changeState(false)
                                }
                            }
                            is DataResult.NotFound -> {
                                withContext(Dispatchers.Main) {
                                    navController.navigate(R.id.action_authorizationFragment_to_physicalIndexesFragment)
                                    viewModel.changeState(false)
                                }
                            }
                            is DataResult.Error -> {
                                withContext(Dispatchers.Main) {
                                    viewModel.changeState(false)
                                    val prefs =  requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                                    prefs.edit().remove("jwt").commit()
                                }
                            }
                            else -> {
                                withContext(Dispatchers.Main) {
                                    viewModel.changeState(false)
                                    val prefs =  requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                                    prefs.edit().remove("jwt").commit()
                                }
                            }
                        }
                    }
                }
                is AuthResult.Unauthorized -> {
                    withContext(Dispatchers.Main) {
                        viewModel.changeState(false)
                        val prefs =  requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                        prefs.edit().remove("jwt").commit()
                    }
                }
                is AuthResult.Error -> {
                    withContext(Dispatchers.Main) {
                        viewModel.changeState(false)
                        val prefs =  requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                        prefs.edit().remove("jwt").commit()
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            if (it.checking) {
                binding.progressBar?.visibility = ProgressBar.VISIBLE
                binding.enterBtn.isEnabled = false
                binding.regBtn.isEnabled = false
                binding.linearLayoutEditTexts?.visibility = LinearLayout.INVISIBLE
                binding.linearLayoutFitness.visibility = LinearLayout.INVISIBLE
                binding.enterBtn.visibility = Button.INVISIBLE
                binding.regBtn.visibility = Button.INVISIBLE
            } else {
                binding.progressBar?.visibility = ProgressBar.INVISIBLE
                binding.enterBtn.isEnabled = true
                binding.regBtn.isEnabled = true
                binding.linearLayoutEditTexts?.visibility = LinearLayout.VISIBLE
                binding.linearLayoutFitness.visibility = LinearLayout.VISIBLE
                binding.enterBtn.visibility = Button.VISIBLE
                binding.regBtn.visibility = Button.VISIBLE
            }
        }

        binding.regBtn.setOnClickListener {
            viewModel.changeState(true)
            navController.navigate(R.id.action_authorizationFragment_to_registrationFragment)
            viewModel.changeState(false)
        }

        binding.enterBtn.setOnClickListener {
            viewModel.changeState(true)
            val login = binding.emailIET.text.toString()
            val pass = binding.passwordIET.text.toString()
            if (login.isNotEmpty() and
                pass.isNotEmpty()
            ) {
                if (isEmailValid(login)) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        when(val result = viewModel.signIn(login, pass).single()) {
                            is AuthResult.Authorized -> {
                                lifecycleScope.launch(Dispatchers.IO) {
                                    when(val dataResult = viewModel.checkUserData().single()) {
                                        is DataResult.Successful -> {
                                            withContext(Dispatchers.Main) {
                                                navController.navigate(R.id.action_authorizationFragment_to_mainFragment)
                                                viewModel.changeState(false)
                                            }
                                        }
                                        is DataResult.NotFound -> {
                                            withContext(Dispatchers.Main) {
                                                navController.navigate(R.id.action_authorizationFragment_to_physicalIndexesFragment)
                                                viewModel.changeState(false)
                                            }
                                        }
                                        is DataResult.Error -> {
                                            withContext(Dispatchers.Main) {
                                                viewModel.changeState(false)
                                            }
                                            Snackbar.make(
                                                binding.root,
                                                dataResult.errorMessage.toString(),
                                                Snackbar.LENGTH_SHORT
                                            )
                                                .setAnimationMode(ANIMATION_MODE_SLIDE)
                                                .show()
                                        }
                                        else -> {
                                            withContext(Dispatchers.Main) {
                                                viewModel.changeState(false)
                                            }
                                            Snackbar.make(
                                                binding.root,
                                                "Что-то не так...",
                                                Snackbar.LENGTH_SHORT
                                            )
                                                .setAnimationMode(ANIMATION_MODE_SLIDE)
                                                .show()
                                        }
                                    }
                                }
                            }
                            is AuthResult.Unauthorized -> { //если авторизация неуспешна, вывод всплывающего сообщения с соответсвующей надписью
                                withContext(Dispatchers.Main) {
                                    viewModel.changeState(false)
                                }
                                Snackbar.make(
                                    binding.root,
                                    "Неуспешная авторизация",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .setAnimationMode(ANIMATION_MODE_SLIDE)
                                    .show()
                            }
                            is AuthResult.Error -> { //в случае ошибки, вывод всплывающего сообщения с ошибкой
                                withContext(Dispatchers.Main) {
                                    viewModel.changeState(false)
                                }
                                Snackbar.make(
                                    binding.root,
                                    result.errorMessage.toString(),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .setAnimationMode(ANIMATION_MODE_SLIDE)
                                    .show()
                            }
                        }
                    }
                } else {
                    viewModel.changeState(false)
                    Snackbar.make(
                        binding.root,
                        "Проверьте почту",
                        Snackbar.LENGTH_SHORT
                    )
                        .setAnimationMode(ANIMATION_MODE_SLIDE)
                        .show()
                }
            } else {
                viewModel.changeState(false)
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



    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}