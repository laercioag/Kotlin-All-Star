package com.laercioag.kotlinallstar.ui.list

import androidx.lifecycle.ViewModel
import com.laercioag.kotlinallstar.data.repository.GitHubRepository
import javax.inject.Inject

class ListViewModel @Inject constructor(
    gitHubRepository: GitHubRepository
) : ViewModel() {

    private val repoResult = gitHubRepository.get()

    val items = repoResult.pagedList
    val networkState = repoResult.repositoryState


    fun refresh() {
        repoResult.refresh.invoke()
    }

}
