package com.laercioag.kotlinallstar.data.repository

import com.laercioag.kotlinallstar.data.local.database.AppDatabase
import com.laercioag.kotlinallstar.data.local.entity.Repository
import com.laercioag.kotlinallstar.data.mapper.RepositoryMapper
import com.laercioag.kotlinallstar.data.remote.api.Api
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import javax.inject.Inject
import javax.inject.Singleton

interface RepositoriesRepository {
    fun get(): Single<List<Repository>>
}

@Singleton
class RepositoriesRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val api: Api,
    private val mapper: RepositoryMapper
) : RepositoriesRepository {
    override fun get(): Single<List<Repository>> = api.get()
        .map { response -> mapper.mapTo(response) }
        .onErrorResumeNext {
            database.repositoryDao().getAll().toSingle()
        }
        .doOnSuccess { list ->
            database.repositoryDao().insertAll(*list.toTypedArray())
            database.repositoryDao().getAll()
        }

}