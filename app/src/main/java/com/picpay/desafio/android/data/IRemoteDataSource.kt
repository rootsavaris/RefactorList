package com.picpay.desafio.android.data

import com.picpay.desafio.android.domain.ApiResponse
import com.picpay.desafio.android.domain.User
import com.picpay.desafio.android.framework.network.UserNetwork
import retrofit2.Call

interface IRemoteDataSource {
    suspend fun getUsers(): List<UserNetwork>
    suspend fun getUsers2(): ApiResponse<List<UserNetwork>>
}

