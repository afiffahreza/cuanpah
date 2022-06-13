package com.exercise.cuanpah.ui.main

import androidx.lifecycle.ViewModel
import com.exercise.cuanpah.data.UserRepository

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser() = userRepository.getUser()

}