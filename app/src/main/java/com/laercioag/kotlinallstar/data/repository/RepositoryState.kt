package com.laercioag.kotlinallstar.data.repository

sealed class RepositoryState {
    object InitialLoadingState : RepositoryState()
    object LoadingState : RepositoryState()
    object LoadedState : RepositoryState()
    data class InitialLoadingErrorState(val throwable: Throwable) : RepositoryState()
    data class ErrorState(val throwable: Throwable) : RepositoryState()
}