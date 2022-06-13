package com.exercise.cuanpah.di

import com.exercise.cuanpah.data.ApiConfig
import com.exercise.cuanpah.data.UserPreference
import com.exercise.cuanpah.data.UserRepository


object Injection {

    fun provideRepository(token: String, pref: UserPreference) : UserRepository {
        val apiService = ApiConfig().getApiService()
        return UserRepository(apiService, token, pref)
    }

}