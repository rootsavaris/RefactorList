package com.picpay.desafio.android.di

import com.picpay.desafio.android.interactors.GetUsersUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single {
        GetUsersUseCase(get())
    }
}
