package com.laercioag.kotlinallstar.data.repository

import com.laercioag.kotlinallstar.data.remote.api.RepositoriesApi
import com.laercioag.kotlinallstar.data.remote.dto.Repositories
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

interface RepositoriesRepository {
    fun get(): Single<Repositories>
}

@Singleton
class RepositoriesRepositoryImpl @Inject constructor(
    private val repositoriesApi: RepositoriesApi
) : RepositoriesRepository {
    override fun get(): Single<Repositories> = repositoriesApi.get()
}