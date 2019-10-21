package com.laercioag.kotlinallstar.data.remote.service

import com.laercioag.kotlinallstar.data.remote.dto.Response
import io.reactivex.Single
import retrofit2.http.GET

interface RemoteService {

    @GET("search/repositories?q=language:kotlin&sort=stars")
    fun getRepositories(): Single<Response>
}