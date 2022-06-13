package com.exercise.cuanpah.ui.profile

import androidx.lifecycle.ViewModel
import com.exercise.cuanpah.data.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser() = userRepository.getUser()

    fun logout() = userRepository.logout()

}