package com.picpay.desafio.android.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.base.BaseAction
import com.picpay.desafio.android.base.BaseViewModel
import com.picpay.desafio.android.base.BaseViewState
import com.picpay.desafio.android.data.DataConsumeType
import com.picpay.desafio.android.domain.ApiResponse
import com.picpay.desafio.android.domain.User
import com.picpay.desafio.android.interactors.GetUsersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application, private val getUsersUseCase: GetUsersUseCase, private val callInit: Boolean = true) : BaseViewModel<MainViewModel.ViewState, MainViewModel.Action>(application, MainViewModel.ViewState()) {

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>>
        get() = _userList

    init {
        if (callInit){
            loadData(DataConsumeType.BOTH)
        }
    }

    fun loadData(dataConsumeType: DataConsumeType){

        viewModelScope.launch {

            getUsersUseCase.execute(GetUsersUseCase.Params(dataConsumeType),
                {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main){
                            sendAction(Action.UsersInitialState)
                        }
                    }
                },
                {
                    if (it is ApiResponse.Success){
                        if (!it.data.isNullOrEmpty()){
                            sendAction(Action.UsersListSuccess)
                            _userList.postValue(it.data)
                        } else {
                            sendAction(Action.UsersListEmpty("Empty list"))
                        }
                    } else if (it is ApiResponse.Failure){
                        sendAction(Action.UsersListError(it.e.message ?: ""))
                    }
                }, {}
            )

        }
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.UsersInitialState -> ViewState(
            isLoading = true
        )
        is Action.UsersListSuccess -> ViewState(
            isLoading = false
        )
        is Action.UsersListEmpty -> ViewState(
            isLoading = false,
            msgEmpty = viewAction.msgEmpty
        )
        is Action.UsersListError -> ViewState(
            isLoading = false,
            msgError = viewAction.msgError
        )
    }

    data class ViewState(val isLoading: Boolean = true,
                         val msgEmpty: String = "",
                         val msgError: String = ""
    ) : BaseViewState

    sealed class Action : BaseAction {
        object UsersInitialState : Action()
        object UsersListSuccess : Action()
        class UsersListEmpty(val msgEmpty: String) : Action()
        class UsersListError(val msgError: String) : Action()
   }

}