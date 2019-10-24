package com.laercioag.kotlinallstar.data.repository

sealed class RepositoryState {
    object InitialLoading: RepositoryState()
    object LoadingState : RepositoryState()
    object LoadedState : RepositoryState()
    data class ErrorState(val throwable: Throwable) : RepositoryState()
}