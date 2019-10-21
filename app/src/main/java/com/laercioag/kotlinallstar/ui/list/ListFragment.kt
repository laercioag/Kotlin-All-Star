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
import com.laercioag.kotlinallstar.R
import com.laercioag.kotlinallstar.data.local.entity.Repository
import com.laercioag.kotlinallstar.data.remote.api.Api
import com.laercioag.kotlinallstar.ui.base.BaseFragment
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class ListFragment : BaseFragment() {

    @Inject
    lateinit var api: Api

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private val stateObserver = Observer<ListViewModel.State> { state ->
        when (state) {
            is ListViewModel.State.LoadingState -> showLoading()
            is ListViewModel.State.ErrorState -> showError(state.throwable)
            is ListViewModel.State.ListState -> showList(state.items)
        }
    }

    private val adapter = ListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, stateObserver)
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
        loader.visibility = View.VISIBLE
    }

    private fun showError(throwable: Throwable) {
        loader.visibility = View.GONE
        Log.e(ListFragment::class.java.simpleName, "Error: ", throwable)
    }

    private fun showList(items: PagedList<Repository>) {
        swipeToRefresh.isRefreshing = false
        loader.visibility = View.GONE
        adapter.submitList(items)
    }

}
