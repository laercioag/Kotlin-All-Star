package com.laercioag.kotlinallstar.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.reactivex.Completable

data class Result<T>(
    val pagedList: LiveData<PagedList<T>>,
    val repositoryState: LiveData<RepositoryState>,
    val refresh: () -> Completable,
    val clear: () -> Unit
)