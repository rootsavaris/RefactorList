package com.picpay.desafio.android.interactors

import com.picpay.desafio.android.base.FlowableUseCase
import com.picpay.desafio.android.data.IRepository
import com.picpay.desafio.android.domain.ApiResponse
import com.picpay.desafio.android.domain.User
import com.picpay.desafio.android.data.DataConsumeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext

class GetUsersUseCase(
    private val iRepository: IRepository,
    coroutineContext: CoroutineContext = Dispatchers.Main
) : FlowableUseCase<ApiResponse<List<User>>, GetUsersUseCase.Params>(coroutineContext) {

    override suspend fun buildUseCaseFlowable(params: Params): Flow<ApiResponse<List<User>>> {
        return iRepository.getUserList(params.dataConsumeType)
    }

    data class Params(val dataConsumeType: DataConsumeType)

}