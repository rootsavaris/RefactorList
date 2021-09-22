package com.picpay.desafio.android.framework.database

import com.picpay.desafio.android.data.ILocalDataSource

class LocalDataSource(private val userDao: UserDao) : ILocalDataSource {

    override suspend fun addUser(user: UserEntity) {
        userDao.addUser(user)
    }

    override suspend fun getUsers(): List<UserEntity> {
        return userDao.getUsers()
    }

}