package com.laercioag.kotlinallstar.data.remote.dto


import com.google.gson.annotations.SerializedName

data class Repositories(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean? = false,
    val items: List<Item?>? = listOf(),
    @SerializedName("total_count")
    val totalCount: Int? = 0
)