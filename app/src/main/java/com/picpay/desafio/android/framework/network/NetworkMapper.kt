package com.picpay.desafio.android.framework.network

import com.picpay.desafio.android.domain.User

fun UserNetwork.toDomain() = User(
    img = this.img,
    id = this.id, name = this.name, username = this.username
)