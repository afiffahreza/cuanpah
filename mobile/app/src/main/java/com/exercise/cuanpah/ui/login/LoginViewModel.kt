package com.exercise.cuanpah.ui.login


import androidx.lifecycle.ViewModel
import com.exercise.cuanpah.data.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser() = userRepository.getUser()
    fun loginUser(email: String, password: String) = userRepository.loginUser(email, password)

}