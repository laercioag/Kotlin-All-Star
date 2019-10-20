package com.laercioag.kotlinallstar.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laercioag.kotlinallstar.R
import com.laercioag.kotlinallstar.data.remote.dto.Item
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var items: List<Item> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item) {
            with(itemView) {
                Glide.with(this).load(item.owner?.avatarUrl).into(avatar)
                name.text = item.fullName
                author.text = item.owner?.login
                stars.text = "${item.stargazersCount} stars"
                forks.text = "${item.forksCount} forks"
            }
        }
    }
}