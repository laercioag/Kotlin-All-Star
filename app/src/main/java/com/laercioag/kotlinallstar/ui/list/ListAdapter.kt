package com.laercioag.kotlinallstar.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laercioag.kotlinallstar.R
import com.laercioag.kotlinallstar.data.local.entity.Repository
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var items: List<Repository> = listOf()
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
        fun bind(item: Repository) {
            with(itemView) {
                Glide.with(this).load(item.avatarUrl).into(avatar)
                name.text = item.fullName
                author.text = item.author
                stars.text = "${item.stars} stars"
                forks.text = "${item.forks} forks"
            }
        }
    }
}