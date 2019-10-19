package com.laercioag.kotlinallstar.ui.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laercioag.kotlinallstar.data.remote.dto.Repositories
import com.laercioag.kotlinallstar.data.repository.RepositoriesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val repositoriesRepository: RepositoriesRepository
) : ViewModel() {

    val repositories = MutableLiveData<Repositories>()

    private val compositeDisposable = CompositeDisposable()

    init {
        getRepositories()
    }

    private fun getRepositories() {
        compositeDisposable.add(
            repositoriesRepository.get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = {
                        Log.e(ListFragment::class.java.simpleName, "Error: ", it)
                    },
                    onSuccess = {
                        repositories.postValue(it)
                    }
                )
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}
