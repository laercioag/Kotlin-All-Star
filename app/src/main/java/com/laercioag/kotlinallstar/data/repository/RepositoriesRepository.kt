package com.laercioag.kotlinallstar.data.repository

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.laercioag.kotlinallstar.data.local.database.AppDatabase
import com.laercioag.kotlinallstar.data.local.entity.Repository
import com.laercioag.kotlinallstar.data.mapper.RepositoryMapper
import com.laercioag.kotlinallstar.data.remote.api.Api
import com.laercioag.kotlinallstar.data.repository.RepositoriesRepository.Companion.PAGE_SIZE
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

interface RepositoriesRepository {

    companion object {
        const val PAGE_SIZE = 100
    }

    fun get(): Flowable<PagedList<Repository>>

    fun invalidate(): Completable
}

@Singleton
class RepositoriesRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val api: Api,
    private val mapper: RepositoryMapper
) : RepositoriesRepository {

    override fun get() = getFromDb()

    private fun getFromDb(): Flowable<PagedList<Repository>> {
        return RxPagedListBuilder(
            database.repositoryDao().getAll(),
            PagedList.Config.Builder().apply {
                setPageSize(PAGE_SIZE)
                setInitialLoadSizeHint(PAGE_SIZE)
                setEnablePlaceholders(false)
            }.build()
        ).setBoundaryCallback(RepositoryBoundaryCallback(api, database, mapper))
            .buildFlowable(BackpressureStrategy.LATEST)
    }

    override fun invalidate(): Completable {
        return Completable.fromCallable {
            database.repositoryDao().deleteAll()
        }
    }
}