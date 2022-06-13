package com.exercise.cuanpah.ui.login

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
import com.exercise.cuanpah.data.UserModel
import com.exercise.cuanpah.data.UserPreference
import com.exercise.cuanpah.databinding.ActivityLoginBinding
import com.exercise.cuanpah.ui.ViewModelFactory
import com.exercise.cuanpah.ui.main.MainActivity
import com.exercise.cuanpah.ui.register.RegisterActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        setupAction()

        binding.toRegisterLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
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
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), "")
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun setupAction() {
        binding.buttonLogin.setOnClickListener {
            showLoading(true)
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()
            when {
                email.isEmpty() -> {
                    showLoading(false)
                    binding.emailLogin.error = "Masukkan email"
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    showLoading(false)
                    binding.emailLogin.error = "Please use email format!"
                }
                password.isEmpty() -> {
                    showLoading(false)
                    binding.passwordLogin.error = "Masukkan password"
                }
                else -> {
                    loginViewModel.loginUser(email, password).observe(this) {
                        when (it) {
                            "200" -> {
                                showLoading(false)
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                Toast.makeText(this@LoginActivity, "WELCOME", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            null -> {
                                showLoading(false)
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Retrofit Instance Error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                showLoading(false)
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Register your account first!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        RegisterActivity::class.java
                                    )
                                )
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