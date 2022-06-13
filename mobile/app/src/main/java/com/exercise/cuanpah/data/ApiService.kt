package com.exercise.cuanpah.data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

data class RegisterResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
)

data class LoginResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
)

data class OrderResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data:OrderResponseData
)

data class OrderGetResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data:List<OrderResponseData>
)

data class GetPointResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<GetPointDataResponse>
)

data class GetPointDataResponse(
    @field:SerializedName("userPointsId")
    val userPointsId: Int,

    @field:SerializedName("userId")
    val userId: Int,

    @field:SerializedName("points")
    val points: Int
)

data class CreatePointResponse(
    @field:SerializedName("message")
    val message: String
)


interface ApiService {

    @POST("/register")
    fun registerUser(@Body registerData: RegisterData ) : Call<RegisterResponse>

    @POST("/login")
    fun loginUser(@Body loginData: LoginData) : Call<LoginResponse>

    @POST("/requests")
    fun requestOrder(@Body orderData: OrderData) : Call<OrderResponse>

    @GET("/requests")
    fun getOrder(@Query("user") user:Int) : Call<OrderGetResponse>

    @GET("/userPoints/{userId}")
    fun getPoint(@Path("userId") userId: Int) : Call<GetPointResponse>

    @POST("/userPoints")
    fun createPoint(@Body createPointData: CreatePointData) : Call<CreatePointResponse>

}

data class RegisterData(
    val email: String,
    val password: String,
    val name: String
)

data class LoginData(
    val email: String,
    val password: String
)

data class OrderData(
    val userId:Int,
    val driverId:Int,
    val lat:Double,
    val lon:Double,
    val status:String,
    val wasteWeight: Double,
    val wasteType:String
)

data class OrderResponseData(
    val userId:Int,
    val driverId:Int,
    val lat:Double,
    val lon:Double,
    val status:String,
    val request_time:String,
    val pickup_time:String,
    val wasteWeight: Double,
    val wasteType: String,
    val driverName:String
)

data class CreatePointData(
    val userId: Int,
    val points: Int
)
