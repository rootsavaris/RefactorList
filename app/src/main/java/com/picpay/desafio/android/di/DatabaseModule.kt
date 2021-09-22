package com.picpay.desafio.android.di

import android.content.Context
import com.picpay.desafio.android.framework.database.PicPayDatabase
import com.picpay.desafio.android.framework.database.UserDao
import org.koin.dsl.module

val databaseModule = module {
    single { provideDataBase(get()) }
    factory { provideUserDao(get()) }
}

fun provideDataBase(context: Context): PicPayDatabase{
    return PicPayDatabase.getInstance(context)
}

fun provideUserDao(gitHubDatabase: PicPayDatabase): UserDao = gitHubDatabase.userDao()




