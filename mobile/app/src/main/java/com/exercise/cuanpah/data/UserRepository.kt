package com.exercise.cuanpah.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(
    private val apiService: ApiService,
    private val token: String,
    private val pref: UserPreference
) {

    private val message = MutableLiveData<String?>()

    // preference
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun login(user: UserModel) {
        CoroutineScope(Dispatchers.IO).launch {
            pref.login(user)
        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            pref.logout()
        }
    }

    fun savePoint(point: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            pref.savePoint(point)
        }
    }

    // func
    fun loginUser(email: String, password: String): LiveData<String?> {
        apiService.loginUser(LoginData(email, password)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        login(UserModel(responseBody.name, responseBody.email, "", true, responseBody.token, responseBody.id,0))
                        message.value = response.code().toString()
                    }
                } else {
                    message.value = response.code().toString()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                message.value = null
            }

        })
        return message
    }

    fun registerUser(email: String, password: String, name: String): LiveData<String?> {
        apiService.registerUser(RegisterData(email, password, name)).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        message.value = response.code().toString()
                    }
                } else {
                    message.value = response.code().toString()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                message.value = null
            }

        })
        return message
    }

    fun getPoint(userId: Int): LiveData<String?> {
        apiService.getPoint(userId).enqueue(object : Callback<GetPointResponse> {
            override fun onResponse(
                call: Call<GetPointResponse>,
                response: Response<GetPointResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody!=null) {
                        if (responseBody.data.isEmpty()) {
                            savePoint(0)
                            createPoint(userId, 0)
                            message.value = response.code().toString()
                            Log.e("POINT", responseBody.data[0].points.toString())
                        } else {
                            savePoint(responseBody.data[0].points)
                            message.value = response.code().toString()
                            Log.e("POINT", responseBody.data[0].points.toString())
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GetPointResponse>, t: Throwable) {
                message.value = null
            }

        })
        return message
    }

    fun createPoint(userId: Int, point: Int) {
        apiService.createPoint(CreatePointData(userId,point)).enqueue(object : Callback<CreatePointResponse> {
            override fun onResponse(
                call: Call<CreatePointResponse>,
                response: Response<CreatePointResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.e("Create Point", "BERHASIL")
                    }
                } else {
                    Log.e("Create Point", "GAGAL")
                }
            }

            override fun onFailure(call: Call<CreatePointResponse>, t: Throwable) {
                Log.e("Create Point", "GAGAL")
            }


        })
    }


}