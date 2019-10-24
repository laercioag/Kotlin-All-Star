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
            val hadExtraRow = isLoading()
            field = value
            val hasExtraRow = isLoading()
            if (hadExtraRow != hasExtraRow) {
                if (hadExtraRow) {
                    notifyItemRemoved(super.getItemCount())
                } else {
                    notifyItemInserted(super.getItemCount())
                }
            } else if (hasExtraRow && previousState != value) {
                notifyItemChanged(itemCount - 1)
            }
        }


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
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading() && position == itemCount - 1) {
            R.layout.loading_item
        } else {
            R.layout.list_item
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (isLoading()) 1 else 0
    }

    private fun isLoading() =
        repositoryState != null && repositoryState == RepositoryState.LoadingState

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Repository) {
            with(itemView) {
                Glide.with(this).load(item.avatarUrl).into(avatar)
                name.text = item.fullName
                author.text = item.author
                stars.text = context.getString(R.string.stars_description, item.stars)
                forks.text = context.getString(R.string.forks_description, item.forks)
            }
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}