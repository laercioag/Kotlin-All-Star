package com.laercioag.kotlinallstar.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.laercioag.kotlinallstar.R
import com.laercioag.kotlinallstar.data.remote.api.RepositoriesApi
import com.laercioag.kotlinallstar.ui.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListFragment : BaseFragment() {

    private lateinit var viewModel: ListViewModel

    @Inject
    lateinit var repositoriesApi: RepositoriesApi

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable.add(
            repositoriesApi.get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = {
                        Log.e(ListFragment::class.java.simpleName, "Error: ", it)
                    },
                    onSuccess = {
                        Log.d(ListFragment::class.java.simpleName, it.toString())
                    }
                )
        )

    }

}
