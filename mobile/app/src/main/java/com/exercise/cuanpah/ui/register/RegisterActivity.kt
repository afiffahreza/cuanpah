package com.exercise.cuanpah.ui.register

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.exercise.cuanpah.data.UserPreference
import com.exercise.cuanpah.databinding.ActivityRegisterBinding
import com.exercise.cuanpah.ui.ViewModelFactory
import com.exercise.cuanpah.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        setupAction()

        binding.textView7.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), "")
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        binding.buttonRegister.setOnClickListener {
            showLoading(true)
            val name = binding.nameTextRegister.text.toString()
            val email = binding.emailTextRegister.text.toString()
            val password = binding.passwordTextRegister.text.toString()
            val confirmPassword = binding.confirmpasswordTextRegister.text.toString()
            when {
                name.isEmpty() -> {
                    showLoading(false)
                    binding.nameTextRegister.error = "Input your name!"
                }
                email.isEmpty() -> {
                    showLoading(false)
                    binding.emailTextRegister.error = "Input your email!"
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    showLoading(false)
                    binding.emailTextRegister.error = "Please use email format!"
                }
                password.isEmpty() -> {
                    showLoading(false)
                    binding.passwordTextRegister.error = "Input your password!"
                }
                confirmPassword.isEmpty() -> {
                    showLoading(false)
                    binding.confirmpasswordTextRegister.error = "Input your password!"
                }
                password!=confirmPassword -> {
                    showLoading(false)
                    binding.confirmpasswordTextRegister.error = "Password doesn't match!"
                }

                else -> {
                    registerViewModel.registerUser(email, password, name).observe(this) {
                        when (it) {
                            "201" -> {
                                showLoading(false)
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Congrats your email is registered!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                            null -> {
                                showLoading(false)
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Error. Try Again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                            else -> {
                                showLoading(false)
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Email is already taken",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }

                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}