package com.picpay.desafio.android

import android.app.Application
import com.picpay.desafio.android.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(networkModule, repositoryModule, databaseModule, viewModelModule, useCaseModule))
        }
    }
}