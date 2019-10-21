package com.laercioag.kotlinallstar.data.remote.api

import com.laercioag.kotlinallstar.data.remote.dto.Response
import com.laercioag.kotlinallstar.data.remote.service.RemoteService
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

interface Api {
    fun get(page: Int, pageSize: Int): Single<Response>
}

@Singleton
class ApiImpl @Inject constructor(
    private val remoteService: RemoteService
) : Api {

    override fun get(page: Int, pageSize: Int): Single<Response> {
        return remoteService.getRepositories(page, pageSize)
    }
}