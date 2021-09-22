package com.picpay.desafio.android.presentation

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.picpay.desafio.android.data.DataConsumeType
import com.picpay.desafio.android.data.IRepository
import com.picpay.desafio.android.domain.ApiResponse
import com.picpay.desafio.android.domain.User
import com.picpay.desafio.android.interactors.GetUsersUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import java.lang.Exception

class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: MainViewModel
    private lateinit var application: Application
    private lateinit var getUsersUseCase: GetUsersUseCase

    @MockK
    private lateinit var repository: IRepository

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        application = mockkClass(Application::class)
        getUsersUseCase = GetUsersUseCase(repository, dispatcher)
        viewModel = MainViewModel(application, getUsersUseCase, false)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify loading state`() {

        runBlockingTest {

            val flow: Flow<ApiResponse<List<User>>> = flow {
                emit(ApiResponse.success(listOf()))
            }

            coEvery { repository.getUserList(any()) } coAnswers { flow }

            val mockedObserver = createStateObserver()
            viewModel.state.observeForever(mockedObserver)

            viewModel.loadData(DataConsumeType.BOTH)

            val slots = mutableListOf<MainViewModel.ViewState>()
            verify { mockedObserver.onChanged(capture(slots)) }

            val loadingState = slots[0]
            Assert.assertTrue(loadingState.isLoading)

        }
    }

    @Test
    fun `verify empty message when service result 0`() {

        runBlockingTest {

            val flow: Flow<ApiResponse<List<User>>> = flow {
                emit(ApiResponse.success(listOf()))
            }

            coEvery { repository.getUserList(any()) } coAnswers { flow }

            val mockedObserver = createStateObserver()
            viewModel.state.observeForever(mockedObserver)

            viewModel.loadData(DataConsumeType.BOTH)

            val slots = mutableListOf<MainViewModel.ViewState>()
            verify { mockedObserver.onChanged(capture(slots)) }

            Assert.assertTrue(slots.size == 2)

            val loadingState = slots[0]
            Assert.assertTrue(loadingState.isLoading)

            val emptyState = slots[1]
            Assert.assertTrue(emptyState.msgEmpty.isNotEmpty())

        }
    }

    @Test
    fun `verify error message when service result exception`() {

        runBlockingTest {

            val flow: Flow<ApiResponse<List<User>>> = flow {
                emit(ApiResponse.failure(Exception("connection error")))
            }

            coEvery { repository.getUserList(any()) } coAnswers { flow }

            val mockedObserver = createStateObserver()
            viewModel.state.observeForever(mockedObserver)

            viewModel.loadData(DataConsumeType.BOTH)

            val slots = mutableListOf<MainViewModel.ViewState>()
            verify { mockedObserver.onChanged(capture(slots)) }

            Assert.assertTrue(slots.size == 2)

            val loadingState = slots[0]
            Assert.assertTrue(loadingState.isLoading)

            val emptyState = slots[1]
            Assert.assertTrue(emptyState.msgError.isNotEmpty())

        }
    }

    @Test
    fun `verify list users when service result one user or more`() {

        runBlockingTest {

            val flow: Flow<ApiResponse<List<User>>> = flow {
                emit(ApiResponse.success(listOf(User("url", "User", 0, "user_1"))))
            }

            coEvery { repository.getUserList(any()) } coAnswers { flow }

            val mockedStateObserver = createStateObserver()
            viewModel.state.observeForever(mockedStateObserver)

            val mockedListObserver = createListUsersObserver()
            viewModel.userList.observeForever(mockedListObserver)

            viewModel.loadData(DataConsumeType.BOTH)

            val slots = mutableListOf<MainViewModel.ViewState>()
            verify { mockedStateObserver.onChanged(capture(slots)) }

            val slotsList = mutableListOf<List<User>>()
            verify { mockedListObserver.onChanged(capture(slotsList)) }

            Assert.assertTrue(slots.size == 2)

            val loadingState = slots[0]
            Assert.assertTrue(loadingState.isLoading)

            val emptyState = slots[1]
            Assert.assertTrue(!emptyState.isLoading)

            Assert.assertTrue(slotsList.size == 1)

            val listState = slotsList[0]
            Assert.assertTrue(listState.size == 1)

        }
    }

    private fun createStateObserver(): Observer<MainViewModel.ViewState> =
        spyk(Observer {})
    private fun createListUsersObserver(): Observer<List<User>> =
        spyk(Observer {})

}