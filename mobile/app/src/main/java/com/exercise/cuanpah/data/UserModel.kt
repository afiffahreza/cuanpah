package com.exercise.cuanpah.data

data class UserModel(
    val name: String,
    val email: String,
    val password: String,
    val isLogin: Boolean,
    val token: String,
    val id: Int,
    val point: Int
)

