package com.laercioag.kotlinallstar.data.remote.service

import com.laercioag.kotlinallstar.data.remote.dto.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {

    @GET("search/repositories?q=language:kotlin&sort=stars")
    fun getRepositories(@Query("page") page: Int, @Query("per_page") pageSize: Int): Single<Response>
}