package com.picpay.desafio.android.framework.network

import com.picpay.desafio.android.data.IRemoteDataSource
import com.picpay.desafio.android.domain.ApiResponse

class RemoteDataSource(private val picPayService: PicPayService): IRemoteDataSource {
    override suspend fun getUsers(): List<UserNetwork> {
        return picPayService.getUsers()
    }

    override suspend fun getUsers2(): ApiResponse<List<UserNetwork>> {
        return try{
            ApiResponse.success(picPayService.getUsers())
        } catch (exception: Exception) {
            ApiResponse.failure(exception)
        }
    }
}