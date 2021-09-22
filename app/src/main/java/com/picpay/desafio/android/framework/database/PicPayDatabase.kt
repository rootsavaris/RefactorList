package com.picpay.desafio.android.framework.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PicPayDatabase : RoomDatabase() {

  companion object {

    private const val DATABASE_NAME = "picpay.db"

    private var instance: PicPayDatabase? = null

    private fun create(context: Context): PicPayDatabase =
        Room.databaseBuilder(context, PicPayDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()


    fun getInstance(context: Context): PicPayDatabase =
        (instance ?: create(context)).also { instance = it }
  }

  abstract fun userDao(): UserDao

}