package com.picpay.desafio.android.di

import com.picpay.desafio.android.data.ILocalDataSource
import com.picpay.desafio.android.data.IRemoteDataSource
import com.picpay.desafio.android.data.IRepository
import com.picpay.desafio.android.data.Repository
import com.picpay.desafio.android.framework.database.LocalDataSource
import com.picpay.desafio.android.framework.database.UserDao
import com.picpay.desafio.android.framework.network.PicPayService
import com.picpay.desafio.android.framework.network.RemoteDataSource
import org.koin.dsl.module

val repositoryModule = module {
    factory { provideLocalDataSource(get()) }
    factory { provideRemoteDataSource(get()) }
    single { provideRepository(get(), get()) }
}

fun provideRepository(iLocalDataSource: ILocalDataSource, iRemoteDataSource: IRemoteDataSource): IRepository {
    return Repository(iLocalDataSource, iRemoteDataSource)
}

fun provideLocalDataSource(userDao: UserDao): ILocalDataSource{
    return LocalDataSource(userDao)
}

fun provideRemoteDataSource(picPayService: PicPayService): IRemoteDataSource{
    return RemoteDataSource(picPayService)
}
