package com.picpay.desafio.android.framework.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int?=null,
    @ColumnInfo(name = "img") val img: String?=null,
    @ColumnInfo(name = "name") val name: String?=null,
    @ColumnInfo(name = "username") val username: String?=null
)

