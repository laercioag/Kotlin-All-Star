package com.laercioag.kotlinallstar.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laercioag.kotlinallstar.R
import com.laercioag.kotlinallstar.data.local.entity.Repository
import com.laercioag.kotlinallstar.data.repository.RepositoryState
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.retry_item.view.*

class ListAdapter : PagedListAdapter<Repository, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        oldItem == newItem
}) {

    var repositoryState: RepositoryState? = null
        set(value) {
            val previousState = field
            val hadExtraRow = isLoading() || isError()
            field = value
            val hasExtraRow = isLoading() || isError()
            if (hadExtraRow != hasExtraRow) {
                if (super.getItemCount() > 0) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                }
            } else if (hasExtraRow && previousState != value) {
                notifyItemChanged(itemCount - 1)
            }
        }

    var retryFunction: (() -> Unit)? = null

    private fun isLoading() =
        repositoryState != null && repositoryState is RepositoryState.LoadingState

    private fun isError() =
        repositoryState != null && repositoryState is RepositoryState.ErrorState

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.list_item -> ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item,
                    parent,
                    false
                )
            )
            R.layout.loading_item -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.loading_item,
                    parent,
                    false
                )
            )
            R.layout.retry_item -> RetryViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.retry_item,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < super.getItemCount()) {
            getItem(position)?.let { item ->
                when (getItemViewType(position)) {
                    R.layout.list_item -> (holder as ItemViewHolder).bind(item)
                }
            }
        } else {
            when (getItemViewType(position)) {
                R.layout.retry_item -> retryFunction?.apply {
                    (holder as RetryViewHolder).bind(this)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading() && position == itemCount - 1) {
            R.layout.loading_item
        } else if (isError() && position == itemCount - 1) {
            R.layout.retry_item
        } else {
            R.layout.list_item
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (isLoading() || isError()) 1 else 0
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Repository) {
            with(itemView) {
                Glide.with(this).load(item.avatarUrl).into(avatar)
                name.text = item.fullName
                author.text = item.author
                stars.text =
                    resources.getQuantityString(R.plurals.stars_description, item.stars, item.stars)
                forks.text =
                    resources.getQuantityString(R.plurals.forks_description, item.forks, item.forks)
            }
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class RetryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(retry: () -> Unit) {
            with(itemView) {
                retryButton.setOnClickListener {
                    retry()
                }
            }
        }
    }
}