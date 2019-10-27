package com.laercioag.kotlinallstar

import com.laercioag.kotlinallstar.data.remote.api.Api
import com.laercioag.kotlinallstar.data.remote.api.ApiImpl
import com.laercioag.kotlinallstar.data.remote.service.RemoteService
import junit.framework.TestCase.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: Api

    @Before
    fun before() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RemoteService::class.java)

        api = ApiImpl(service)
    }

    @After
    fun after() {
        mockWebServer.shutdown()
    }

    @Test
    fun testWhenGetReturnsListOfRepositories() {
        val page = 1
        val perPage = 10
        val path = "/search/repositories?q=language:kotlin&sort=stars&page=$page&per_page=$perPage"
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("json/repositories.json"))
        mockWebServer.enqueue(mockResponse)
        api.get(page = page, pageSize = perPage).test()
            .assertNoErrors()
            .assertValueCount(1)
        val request = mockWebServer.takeRequest()
        assertEquals(path, request.path)
    }

    @Test
    fun testWhenGetReturnsEmptyListOfRepositories() {
        val page = 1
        val perPage = 10
        val path = "/search/repositories?q=language:kotlin&sort=stars&page=$page&per_page=$perPage"
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJson("json/empty.json"))
        mockWebServer.enqueue(mockResponse)
        api.get(page = page, pageSize = perPage).test()
            .assertNoErrors()
            .assertValueCount(1)
        val request = mockWebServer.takeRequest()
        assertEquals(path, request.path)
    }

    @Test
    fun testWhenGetReturnsError() {
        val page = 1
        val perPage = 10
        val path = "/search/repositories?q=language:kotlin&sort=stars&page=$page&per_page=$perPage"
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)
        api.get(page = page, pageSize = perPage).test()
            .assertNoValues()
        val request = mockWebServer.takeRequest()
        assertEquals(path, request.path)
    }

    @Suppress("SameParameterValue")
    private fun getJson(path: String): String {
        val uri = this::class.java.classLoader!!.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}