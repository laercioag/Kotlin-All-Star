package com.laercioag.kotlinallstar.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.laercioag.kotlinallstar.R
import com.laercioag.kotlinallstar.data.local.entity.Repository
import com.laercioag.kotlinallstar.data.repository.RepositoryState
import com.laercioag.kotlinallstar.ui.base.BaseFragment
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class ListFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private val adapter = ListAdapter()

    private var errorSnackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.items.observe(viewLifecycleOwner, Observer {
            showList(it)
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            adapter.repositoryState = it
            when (it) {
                is RepositoryState.InitialLoading -> showLoading()
                is RepositoryState.ErrorState -> showError(it.throwable)
                is RepositoryState.LoadedState -> hideLoading()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeToRefresh()
        setupRecyclerView()
    }

    private fun setupSwipeToRefresh() {
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = true
            viewModel.refresh()
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                layoutManager.orientation
            )
        )
    }

    private fun showLoading() {
        errorSnackbar?.dismiss()
        swipeToRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        swipeToRefresh.isRefreshing = false
    }

    private fun showError(throwable: Throwable) {
        hideLoading()
        errorSnackbar = Snackbar.make(
            rootLayout,
            getString(R.string.error_message),
            Snackbar.LENGTH_LONG
        ).apply { show() }
        Log.e(ListFragment::class.java.simpleName, "Error: ", throwable)
    }

    private fun showList(items: PagedList<Repository>) {
        adapter.submitList(items)
    }
}
