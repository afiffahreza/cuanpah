package com.exercise.cuanpah.ui.point

import androidx.lifecycle.ViewModel
import com.exercise.cuanpah.data.UserRepository

class PointViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUser() = userRepository.getUser()

    fun getPoint(userId: Int) = userRepository.getPoint(userId)

}