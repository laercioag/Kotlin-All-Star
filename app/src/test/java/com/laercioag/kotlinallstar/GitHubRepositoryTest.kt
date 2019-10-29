package com.laercioag.kotlinallstar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.laercioag.kotlinallstar.data.local.dao.RepositoryDao
import com.laercioag.kotlinallstar.data.local.database.AppDatabase
import com.laercioag.kotlinallstar.data.mapper.RepositoryMapper
import com.laercioag.kotlinallstar.data.remote.api.Api
import com.laercioag.kotlinallstar.data.remote.dto.Response
import com.laercioag.kotlinallstar.data.repository.GitHubRepositoryImpl
import com.laercioag.kotlinallstar.data.repository.RepositoryState
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GitHubRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: Api

    @Mock
    private lateinit var database: AppDatabase

    @Mock
    private lateinit var mapper: RepositoryMapper

    @Mock
    private lateinit var repositoryDao: RepositoryDao

    private lateinit var repository: GitHubRepositoryImpl

    @Before
    fun setup() {
        whenever(database.repositoryDao()).thenReturn(repositoryDao)
        repository = GitHubRepositoryImpl(database, api, mapper)
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @Test
    fun assertGetNewPageLoadedStateGivenThatEverythingSucceeds() {
        whenever(api.get(any(), any())).thenReturn(Single.just(Response()))

        @Suppress("UNCHECKED_CAST")
        val observer = Mockito.mock(Observer::class.java) as Observer<RepositoryState>
        repository.state.observeForever(observer)

        repository.getNewPage(true)

        val inOrder = Mockito.inOrder(observer)
        inOrder.verify(observer).onChanged(RepositoryState.InitialLoadingState)
        inOrder.verify(observer).onChanged(RepositoryState.LoadedState)
        inOrder.verifyNoMoreInteractions()

        assertEquals(RepositoryState.LoadedState, repository.state.value)
    }

    @Test
    fun assertGetNewPageErrorStateGivenThatDatabaseGivesAnError() {
        val exception = Exception()
        whenever(api.get(any(), any())).thenReturn(Single.error(exception))

        @Suppress("UNCHECKED_CAST")
        val observer = Mockito.mock(Observer::class.java) as Observer<RepositoryState>
        repository.state.observeForever(observer)

        repository.getNewPage(false)

        val inOrder = Mockito.inOrder(observer)
        inOrder.verify(observer).onChanged(RepositoryState.LoadingState)
        inOrder.verify(observer).onChanged(RepositoryState.ErrorState(exception))
        inOrder.verifyNoMoreInteractions()

        assertEquals(RepositoryState.ErrorState(exception), repository.state.value)
    }

    @Test
    fun assertGetNewPageInitialLoadingErrorStateGivenThatDatabaseGivesAnError() {
        val exception = Exception()
        whenever(api.get(any(), any())).thenReturn(Single.error(exception))

        @Suppress("UNCHECKED_CAST")
        val observer = Mockito.mock(Observer::class.java) as Observer<RepositoryState>
        repository.state.observeForever(observer)

        repository.getNewPage(true)

        val inOrder = Mockito.inOrder(observer)
        inOrder.verify(observer).onChanged(RepositoryState.InitialLoadingState)
        inOrder.verify(observer).onChanged(RepositoryState.InitialLoadingErrorState(exception))
        inOrder.verifyNoMoreInteractions()

        assertEquals(RepositoryState.InitialLoadingErrorState(exception), repository.state.value)
    }

    @Test
    fun assertRefreshLoadedStateGivenThatEverythingSucceeds() {
        whenever(api.get(any(), any())).thenReturn(Single.just(Response()))

        @Suppress("UNCHECKED_CAST")
        val observer = Mockito.mock(Observer::class.java) as Observer<RepositoryState>
        repository.state.observeForever(observer)

        repository.refresh()

        val inOrder = Mockito.inOrder(observer)
        inOrder.verify(observer).onChanged(RepositoryState.InitialLoadingState)
        inOrder.verify(observer).onChanged(RepositoryState.LoadedState)
        inOrder.verifyNoMoreInteractions()

        assertEquals(RepositoryState.LoadedState, repository.state.value)
    }
}