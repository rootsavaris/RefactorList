package com.picpay.desafio.android.data

import com.picpay.desafio.android.domain.ApiResponse
import com.picpay.desafio.android.domain.User
import kotlinx.coroutines.flow.Flow

interface IRepository {
    fun getUserList(consumeType: DataConsumeType): Flow<ApiResponse<List<User>>>
}