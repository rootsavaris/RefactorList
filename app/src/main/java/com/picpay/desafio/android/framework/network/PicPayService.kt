package com.picpay.desafio.android.framework.network

import retrofit2.Call
import retrofit2.http.GET


interface PicPayService {

    @GET("users")
    suspend fun getUsers(): List<UserNetwork>

}