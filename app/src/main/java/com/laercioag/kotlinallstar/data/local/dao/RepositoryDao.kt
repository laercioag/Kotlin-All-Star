package com.laercioag.kotlinallstar.data.local.dao

import androidx.room.*
import com.laercioag.kotlinallstar.data.local.entity.Repository

@Dao
interface RepositoryDao {

    @Query("SELECT * FROM repository ORDER BY stars DESC")
    fun getAll(): List<Repository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg repos: Repository)

    @Delete
    fun delete(repo: Repository)
}