package com.laercioag.kotlinallstar.ui.list

import androidx.lifecycle.ViewModel
import com.laercioag.kotlinallstar.data.repository.GitHubRepository
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel @Inject constructor(
    gitHubRepository: GitHubRepository
) : ViewModel() {

    private val repoResult = gitHubRepository.get()

    val items = repoResult.pagedList
    val networkState = repoResult.repositoryState

    fun refresh() {
        repoResult.refresh()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
    }

    override fun onCleared() {
        repoResult.clear()
        super.onCleared()
    }
}
