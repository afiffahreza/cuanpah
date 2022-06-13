package com.exercise.cuanpah.ui.maps

import androidx.lifecycle.ViewModel
import com.exercise.cuanpah.data.UserRepository

class MapsViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUser() = userRepository.getUser()
}