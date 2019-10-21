package com.laercioag.kotlinallstar.data.repository

import android.util.Log
import androidx.paging.PagedList
import com.laercioag.kotlinallstar.data.local.database.AppDatabase
import com.laercioag.kotlinallstar.data.local.entity.Repository
import com.laercioag.kotlinallstar.data.mapper.RepositoryMapper
import com.laercioag.kotlinallstar.data.remote.api.Api
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RepositoryBoundaryCallback(
    private val api: Api,
    private val database: AppDatabase,
    private val mapper: RepositoryMapper
) : PagedList.BoundaryCallback<Repository>() {

    private var isLoading = false


    override fun onZeroItemsLoaded() {
        loadItems()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Repository) {
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
                    requestPage((size / RepositoriesRepository.PAGE_SIZE) + 1)
                },
                onError = {
                    requestPage(1)
                }
            )
    }

    private fun requestPage(pageNumber: Int) {
        api.get(pageNumber, RepositoriesRepository.PAGE_SIZE)
            .map { response -> mapper.mapTo(response) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doFinally { isLoading = false }
            .subscribeBy(
                onError = {
                    Log.e(RepositoryBoundaryCallback::class.java.simpleName, "Error:", it)
                },
                onSuccess = { repositories ->
                    database.repositoryDao().insertAll(*repositories.toTypedArray())
                }
            )
    }
}