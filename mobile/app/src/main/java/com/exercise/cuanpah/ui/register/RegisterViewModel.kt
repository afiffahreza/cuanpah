package com.exercise.cuanpah.ui.register

import androidx.lifecycle.ViewModel
import com.exercise.cuanpah.data.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun registerUser(email: String, password: String, name: String) = userRepository.registerUser(email, password, name)
}