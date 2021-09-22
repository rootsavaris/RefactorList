package com.picpay.desafio.android.data

import com.picpay.desafio.android.framework.database.UserEntity

interface ILocalDataSource {
    suspend fun addUser(user: UserEntity)
    suspend fun getUsers(): List<UserEntity>
}