package com.picpay.desafio.android.data

import com.picpay.desafio.android.domain.ApiResponse
import com.picpay.desafio.android.domain.User
import com.picpay.desafio.android.framework.database.fromDomain
import com.picpay.desafio.android.framework.database.toDomain
import com.picpay.desafio.android.framework.network.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository constructor(private val iLocalDataSource: ILocalDataSource, private val iRemoteDataSource: IRemoteDataSource) : IRepository {

    override fun getUserList(consumeType: DataConsumeType): Flow<ApiResponse<List<User>>> {
       return flow{
           if (consumeType == DataConsumeType.INTERNET || consumeType == DataConsumeType.BOTH){
                val apiResponse = iRemoteDataSource.getUsers2()
                if (apiResponse is ApiResponse.Success){
                   val list = apiResponse.data.map { it.toDomain() }
                   if (consumeType == DataConsumeType.INTERNET){
                       emit(ApiResponse.success(list))
                   } else if (consumeType == DataConsumeType.BOTH){
                       for (user in list){
                           iLocalDataSource.addUser(user.fromDomain())
                       }
                       emit(ApiResponse.success(iLocalDataSource.getUsers().map {it.toDomain()}))
                   }
               } else if (apiResponse is ApiResponse.Failure){
                   emit(ApiResponse.failure(apiResponse.e))
               }
            } else {
                emit(ApiResponse.success(iLocalDataSource.getUsers().map {it.toDomain()}))
            }
        }
    }
}