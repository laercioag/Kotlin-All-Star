package com.laercioag.kotlinallstar.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.laercioag.kotlinallstar.data.local.database.AppDatabase
import com.laercioag.kotlinallstar.data.local.entity.Repository
import com.laercioag.kotlinallstar.data.mapper.RepositoryMapper
import com.laercioag.kotlinallstar.data.remote.api.Api
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RepositoryBoundaryCallback(
    private val api: Api,
    private val database: AppDatabase,
    private val mapper: RepositoryMapper
) : PagedList.BoundaryCallback<Repository>() {

    private var isLoading = false

    val repositoryState = MutableLiveData<RepositoryState>()
    val compositeDisposable = CompositeDisposable()

    override fun onZeroItemsLoaded() {
        repositoryState.value = RepositoryState.InitialLoading
        loadItems()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Repository) {
        repositoryState.value = RepositoryState.LoadingState
        loadItems()
    }

    private fun loadItems() {
        if (isLoading) return
        isLoading = true
        Single.fromCallable {
            database.repositoryDao().size()
        }.subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { size ->
                    requestPage((size / GitHubRepository.PAGE_SIZE) + 1)
                },
                onError = {
                    repositoryState.postValue(RepositoryState.ErrorState(it))
                }
            )
    }

    private fun requestPage(pageNumber: Int) {
        compositeDisposable.add(
            api.get(pageNumber, GitHubRepository.PAGE_SIZE)
                .map { response -> mapper.mapTo(response) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doFinally { isLoading = false }
                .subscribeBy(
                    onError = {
                        repositoryState.postValue(RepositoryState.ErrorState(it))
                    },
                    onSuccess = { repositories ->
                        repositoryState.postValue(RepositoryState.LoadedState)
                        database.repositoryDao().insertAll(*repositories.toTypedArray())
                    }
                )
        )
    }
}