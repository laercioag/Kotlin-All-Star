package com.laercioag.kotlinallstar.data.repository

import androidx.paging.toLiveData
import com.laercioag.kotlinallstar.data.local.database.AppDatabase
import com.laercioag.kotlinallstar.data.local.entity.Repository
import com.laercioag.kotlinallstar.data.mapper.RepositoryMapper
import com.laercioag.kotlinallstar.data.remote.api.Api
import com.laercioag.kotlinallstar.data.repository.GitHubRepository.Companion.PAGE_SIZE
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

interface GitHubRepository {

    companion object {
        const val PAGE_SIZE = 30
    }

    fun get(): Result<Repository>
    fun refresh(): Completable
    fun clear()
}

@Singleton
class GitHubRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    api: Api,
    mapper: RepositoryMapper
) : GitHubRepository {

    private val boundaryCallback = RepositoryBoundaryCallback(api, database, mapper)

    override fun get() = getFromDb()

    private fun getFromDb(): Result<Repository> {
        val pagedList = database.repositoryDao().getAll().toLiveData(
            pageSize = PAGE_SIZE,
            boundaryCallback = boundaryCallback
        )

        return Result(
            repositoryState = boundaryCallback.repositoryState,
            pagedList = pagedList,
            refresh = this::refresh,
            clear = this::clear
        )
    }

    override fun refresh(): Completable {
        val state = boundaryCallback.repositoryState
        return Completable.fromCallable {
            boundaryCallback.compositeDisposable.clear()
            database.repositoryDao().deleteAll()
        }.doOnSubscribe {
            state.postValue(RepositoryState.LoadingState)
        }.doOnComplete {
            state.postValue(RepositoryState.LoadedState)
            getFromDb()
        }.doOnError {
            state.postValue(RepositoryState.ErrorState(it))
        }
    }

    override fun clear() {
        boundaryCallback.compositeDisposable.clear()
    }
}