package com.laercioag.kotlinallstar.data.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.paging.toLiveData
import com.laercioag.kotlinallstar.data.local.database.AppDatabase
import com.laercioag.kotlinallstar.data.local.entity.Repository
import com.laercioag.kotlinallstar.data.mapper.RepositoryMapper
import com.laercioag.kotlinallstar.data.remote.api.Api
import com.laercioag.kotlinallstar.data.repository.GitHubRepository.Companion.PAGE_SIZE
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface GitHubRepository {

    companion object {
        const val PAGE_SIZE = 30
    }

    fun get(): Result<Repository>
    fun refresh()
    fun getNewPage(isFirstLoad: Boolean)
    fun loadNewPage(isFirstLoad: Boolean)
    fun clear()
}

@Singleton
class GitHubRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val api: Api,
    private val mapper: RepositoryMapper
) : GitHubRepository {

    private val boundaryCallback = RepositoryBoundaryCallback(this)

    private val compositeDisposable = CompositeDisposable()

    val state = MutableLiveData<RepositoryState>()

    private var isLoading = false

    override fun get() = getFromDb()

    private fun getFromDb(): Result<Repository> {
        val pagedList = database.repositoryDao().getAll().toLiveData(
            pageSize = PAGE_SIZE,
            boundaryCallback = boundaryCallback
        )

        return Result(
            repositoryState = state,
            pagedList = pagedList,
            refresh = this::refresh,
            retry = { getNewPage(isFirstLoad = false) },
            clear = this::clear
        )
    }

    override fun refresh() {
        Completable
            .fromCallable {
                compositeDisposable.clear()
                database.repositoryDao().deleteAll()
            }
            .doOnComplete {
                getNewPage(isFirstLoad = true)
            }
            .doOnError {
                state.postValue(RepositoryState.ErrorState(it))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
    }

    override fun getNewPage(isFirstLoad: Boolean) {
        isLoading = true
        compositeDisposable.add(
            Single
                .fromCallable {
                    database.repositoryDao().size()
                }
                .flatMap { size ->
                    val pageNumber = (size / PAGE_SIZE) + 1
                    api.get(pageNumber, PAGE_SIZE)
                }
                .map { response ->
                    val repositories = mapper.mapTo(response)
                    database.repositoryDao().insertAll(*repositories.toTypedArray())
                }
                .doFinally { isLoading = false }
                .doOnSubscribe {
                    if (isFirstLoad) {
                        state.postValue(RepositoryState.InitialLoadingState)
                    } else {
                        state.postValue(RepositoryState.LoadingState)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = {
                        state.postValue(RepositoryState.LoadedState)
                    },
                    onError = {
                        if (isFirstLoad) {
                            state.postValue(RepositoryState.InitialLoadingErrorState(it))
                        } else {
                            state.postValue(RepositoryState.ErrorState(it))
                        }
                    }
                )
        )
    }

    override fun loadNewPage(isFirstLoad: Boolean) {
        if (isLoading) return
        getNewPage(isFirstLoad)
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}