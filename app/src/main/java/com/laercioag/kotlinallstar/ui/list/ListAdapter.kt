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
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter : PagedListAdapter<Repository, ListAdapter.ViewHolder>(object :
    DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
}