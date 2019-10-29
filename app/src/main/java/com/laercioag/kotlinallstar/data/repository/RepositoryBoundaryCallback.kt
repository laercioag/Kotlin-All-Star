package com.laercioag.kotlinallstar.data.repository

import androidx.paging.PagedList
import com.laercioag.kotlinallstar.data.local.entity.Repository

class RepositoryBoundaryCallback(
    private val repository: GitHubRepository
) : PagedList.BoundaryCallback<Repository>() {

    override fun onZeroItemsLoaded() {
        repository.loadNewPage(isFirstLoad = true)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Repository) {
        repository.loadNewPage(isFirstLoad = false)
    }
}