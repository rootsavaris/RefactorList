package com.picpay.desafio.android.framework.database

import com.picpay.desafio.android.domain.User

fun UserEntity.toDomain() = User(
    img = this.img,
    id = this.id, name = this.name, username = this.username
)

fun User.fromDomain() = UserEntity(
    img = this.img,
    id = this.id, name = this.name, username = this.username
)
