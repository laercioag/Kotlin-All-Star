package com.laercioag.kotlinallstar.data.remote.service

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.lockAndWaitNanos
import java.util.*

class CustomInterceptor : Interceptor {

    companion object {
        private const val RATE_LIMIT_REMAINING = "X-RateLimit-Remaining"
        private const val RATE_LIMIT_RESET = "X-RateLimit-Reset"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        if (!response.isSuccessful) {
            val limitRemaining = getRateLimitRemaining(response)
            if (limitRemaining == 0) {
                val resetDateInSeconds = getRateLimitResetTimeInEpoch(response)
                val intervalInNanoSeconds =
                    getTimeRemainingToResetInNanoSeconds(resetDateInSeconds)
                lockAndWaitNanos(intervalInNanoSeconds)
                response.close()
                response = chain.proceed(request)
            }
        }
        return response
    }

    private fun getRateLimitRemaining(response: Response) =
        response.header(RATE_LIMIT_REMAINING, "0")?.toInt() ?: 0

    private fun getRateLimitResetTimeInEpoch(response: Response) =
        response.header(RATE_LIMIT_RESET, "0")?.toLong() ?: 0L

    private fun getTimeRemainingToResetInNanoSeconds(resetDateInSeconds: Long): Long {
        val resetDate = Date(resetDateInSeconds * 1000)
        val currentDate = Date()
        return (resetDate.time - currentDate.time) * 1000000
    }
}