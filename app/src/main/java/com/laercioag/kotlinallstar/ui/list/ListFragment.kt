package com.laercioag.kotlinallstar.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.laercioag.kotlinallstar.R
import com.laercioag.kotlinallstar.data.remote.api.RepositoriesApi
import com.laercioag.kotlinallstar.ui.base.BaseFragment
import javax.inject.Inject

class ListFragment : BaseFragment() {

    @Inject
    lateinit var repositoriesApi: RepositoriesApi

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

}
