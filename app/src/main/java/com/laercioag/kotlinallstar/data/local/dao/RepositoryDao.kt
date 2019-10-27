package com.laercioag.kotlinallstar.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.laercioag.kotlinallstar.data.local.entity.Repository

@Dao
interface RepositoryDao {

    @Query("SELECT COUNT(id) FROM repository")
    fun size(): Int

    @Query("SELECT * FROM repository ORDER BY stars DESC")
    fun getAll(): DataSource.Factory<Int, Repository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg repos: Repository)

    @Query("DELETE FROM repository")
    fun deleteAll()
}