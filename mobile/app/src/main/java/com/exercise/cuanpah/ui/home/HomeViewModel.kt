package com.exercise.cuanpah.ui.home

import androidx.lifecycle.ViewModel
import com.exercise.cuanpah.data.UserRepository

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser() = userRepository.getUser()

}