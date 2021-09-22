package com.picpay.desafio.android.framework.database

import androidx.room.Dao
import androidx.room.Insert

import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface UserDao {

  @Insert(onConflict = REPLACE)
  suspend fun addUser(user: UserEntity)

  @Query("SELECT * FROM user")
  suspend fun getUsers(): List<UserEntity>

}
