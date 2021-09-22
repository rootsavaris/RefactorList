package com.picpay.desafio.android

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.data.DataConsumeType
import com.picpay.desafio.android.data.IRepository
import com.picpay.desafio.android.domain.ApiResponse
import com.picpay.desafio.android.domain.User
import com.picpay.desafio.android.presentation.MainActivity
import io.mockk.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import java.lang.Exception

class MainActivityTest {

    private lateinit var module: Module

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var repositoryMocked: IRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Before
    fun before() {

        repositoryMocked = RepositoryMocked()

        module = module(createdAtStart = true, override = true) {
            single { repositoryMocked }
        }

        loadKoinModules(module)
    }

    @After
    fun after() {
        unloadKoinModules(module)
    }

    @Test
    fun shouldDisplayTitle() {
        launchActivity<MainActivity>().apply {
            val expectedTitle = context.getString(R.string.title)
            moveToState(Lifecycle.State.RESUMED)
            onView(withText(expectedTitle)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun loadSucessOneUser() {
        mockkObject(RepositoryMocked.Companion)
        every { RepositoryMocked.Companion.getCenarioType() } answers { CENARIO_TYPE.LIST_WITH_USERS }
        launchActivity<MainActivity>().apply {
            onView(CoreMatchers.allOf(withId(R.id.recyclerView), isDisplayed())).check(RecyclerViewItemCountAssertion(1))
        }
    }

    @Test
    fun shouldShowToastWithMessageWhenEmptyList(){
        mockkObject(RepositoryMocked.Companion)
        every { RepositoryMocked.Companion.getCenarioType() } answers { CENARIO_TYPE.EMPTY_LIST }
        launchActivity<MainActivity>().apply {
            onView(withId(R.id.txt_status)).check(matches(withText("Empty list")));
        }
    }

    @Test
    fun shouldShowToastWithMessageWhenError(){
        mockkObject(RepositoryMocked.Companion)
        every { RepositoryMocked.Companion.getCenarioType() } answers { CENARIO_TYPE.LOAD_ERROR }
        launchActivity<MainActivity>().apply {
            onView(withId(R.id.txt_status)).check(matches(withText("MSG ERROR")))
        }
    }

    class RepositoryMocked : IRepository {

        private var time = 50L

        companion object {
            fun getCenarioType(): CENARIO_TYPE = CENARIO_TYPE.LIST_WITH_USERS
        }

        override fun getUserList(consumeType: DataConsumeType): Flow<ApiResponse<List<User>>> {
            when {
                getCenarioType() == CENARIO_TYPE.LIST_WITH_USERS -> {
                    return flow {
                        delay(time)
                        emit(
                            ApiResponse.success(
                                listOf(
                                    User(
                                        "https://randomuser.me/api/portraits/men/9.jpg",
                                        "Eduardo Santos",
                                        1001,
                                        "@eduardo.santos"
                                    )
                                )
                            )
                        )
                    }
                }
                getCenarioType() == CENARIO_TYPE.EMPTY_LIST -> {
                    return flow {
                        delay(time)
                        emit(ApiResponse.success(listOf()))
                    }
                }
                else -> {
                    return flow {
                        delay(time)
                        emit(ApiResponse.failure(Exception("MSG ERROR")))
                    }
                }
            }
        }
    }

    enum class CENARIO_TYPE {
        EMPTY_LIST,
        LIST_WITH_USERS,
        LOAD_ERROR
    }

    class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            ViewMatchers.assertThat(adapter!!.itemCount, Matchers.`is`(expectedCount))
        }
    }

}