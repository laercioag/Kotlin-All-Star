package com.laercioag.kotlinallstar.data.remote.api

import com.laercioag.kotlinallstar.data.remote.dto.Repositories
import com.laercioag.kotlinallstar.data.remote.service.RemoteService
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

interface RepositoriesApi {
    fun get(): Single<Repositories>
}

@Singleton
class RepositoriesApiImpl @Inject constructor(
    private val remoteService: RemoteService
) : RepositoriesApi {

    override fun get(): Single<Repositories> {
        return remoteService.getRepositories()
    }
}