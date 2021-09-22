package com.picpay.desafio.android.domain

sealed class ApiResponse<T> {

    data class Success<T>(var data: T) : ApiResponse<T>()
    data class Failure<T>(val e: Exception) : ApiResponse<T>()

    companion object {
        fun <T> success(data: T): ApiResponse<T> = Success(data)
        fun <T> failure(e: Exception): ApiResponse<T> = Failure(e)
    }
}