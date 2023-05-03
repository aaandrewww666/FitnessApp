package com.example.fitnessapp.fragments

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
import com.example.fitnessapp.api.results.PostResponseResult
import com.example.fitnessapp.databinding.FragmentRegistrationBinding
import com.example.fitnessapp.viewmodels.AuthViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val navController by lazy { findNavController() }

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            if (it.checking) {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                binding.regBtn.isEnabled = false
                binding.linearLayout.visibility = LinearLayout.INVISIBLE
                binding.regBtn.visibility = Button.INVISIBLE
            } else {
                binding.progressBar.visibility = ProgressBar.INVISIBLE
                binding.regBtn.isEnabled = true
                binding.linearLayout.visibility = LinearLayout.VISIBLE
                binding.regBtn.visibility = Button.VISIBLE
            }
        }

        binding.regBtn.setOnClickListener {
            viewModel.changeState(true)
            val login = binding.emailIET.text.toString()
            val username = binding.usernameIET.text.toString()
            val userPassword = binding.passwordIET.text.toString()
            val confirmPass = binding.confirmPasswordIET.text.toString()

            if (login.isNotEmpty() && username.isNotEmpty() && userPassword.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (userPassword == confirmPass) {
                    if(isEmailValid(login)) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            when (val signUpResult = viewModel.signUp(login, username, userPassword).single()) {
                                is PostResponseResult.Successful -> { //если авторизация успешна, переход на следующий экран
                                    withContext(Dispatchers.Main) {
                                        viewModel.changeState(false)
                                        navController.navigate(R.id.action_registrationFragment_to_authorizationFragment)
                                    }
                                    Snackbar.make(
                                        binding.root,
                                        "Успешная регистрация",
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .setAnimationMode(ANIMATION_MODE_SLIDE)
                                        .show()

                                }
                                is PostResponseResult.Error -> {
                                    withContext(Dispatchers.Main) {
                                        viewModel.changeState(false)
                                    }
                                    Snackbar.make(
                                        binding.root,
                                        signUpResult.message,
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
                    Snackbar.make(binding.root,"Пароли не совпадают", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(ANIMATION_MODE_SLIDE)
                        .show()
                }
            } else {
                viewModel.changeState(false)
                Snackbar.make(binding.root,"Заполните все поля",Snackbar.LENGTH_SHORT)
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