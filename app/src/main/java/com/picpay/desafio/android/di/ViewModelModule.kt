package com.picpay.desafio.android.di

import com.picpay.desafio.android.presentation.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(androidApplication(), get())
    }
}
