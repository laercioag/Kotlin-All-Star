package com.laercioag.kotlinallstar.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.laercioag.kotlinallstar.data.local.dao.RepositoryDao
import com.laercioag.kotlinallstar.data.local.entity.Repository

@Database(entities = [Repository::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}