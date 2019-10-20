package com.laercioag.kotlinallstar.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laercioag.kotlinallstar.data.remote.dto.Item
import com.laercioag.kotlinallstar.data.repository.RepositoriesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val repositoriesRepository: RepositoriesRepository
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private val compositeDisposable = CompositeDisposable()

    init {
        getRepositories()
    }

    private fun getRepositories() {
        compositeDisposable.add(
            repositoriesRepository.get()
                .doOnSubscribe {
                    _state.postValue(State.LoadingState)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = { throwable ->
                        _state.postValue(State.ErrorState(throwable))
                    },
                    onSuccess = { repositories ->
                        _state.postValue(State.RepositoriesListState(repositories.items.orEmpty()))
                    }
                )
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    sealed class State {
        object LoadingState : State()
        data class ErrorState(val throwable: Throwable) : State()
        data class RepositoriesListState(val items: List<Item>) : State()
    }

}
