package com.laercioag.kotlinallstar.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Repository(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    val author: String,
    val stars: Int,
    val forks: Int
)