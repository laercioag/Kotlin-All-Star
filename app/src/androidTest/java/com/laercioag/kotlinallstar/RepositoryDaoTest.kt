package com.laercioag.kotlinallstar

import android.content.Context
import androidx.room.Room
import androidx.room.paging.LimitOffsetDataSource
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.laercioag.kotlinallstar.data.local.dao.RepositoryDao
import com.laercioag.kotlinallstar.data.local.database.AppDatabase
import com.laercioag.kotlinallstar.data.local.entity.Repository
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryDaoTest {

    private lateinit var repositoryDao: RepositoryDao

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        repositoryDao = db.repositoryDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun insertAndRetrieveRepository() {
        val repository = mockRepository()
        repositoryDao.insertAll(repository)
        val factory = repositoryDao.getAll()
        val list = (factory.create() as LimitOffsetDataSource).loadRange(0, 1)
        assertThat(list[0], equalTo(repository))
    }

    @Test
    fun insertAndRetrieveManyRepositories() {
        val listSize = 10
        val repositories = mockManyRepositories(listSize)
        repositoryDao.insertAll(*repositories)
        val factory = repositoryDao.getAll()
        val list = (factory.create() as LimitOffsetDataSource).loadRange(0, listSize.plus(1))
        assertThat(list.size, equalTo(listSize))
    }

    @Test
    fun countManyRepositories() {
        val listSize = 10
        val repositories = mockManyRepositories(listSize)
        repositoryDao.insertAll(*repositories)
        val actualSize = repositoryDao.size()
        assertThat(actualSize, equalTo(listSize))
    }

    @Test
    fun insertAndDeleteManyRepositories() {
        val listSize = 15
        val repositories = mockManyRepositories(listSize)
        repositoryDao.insertAll(*repositories)
        repositoryDao.deleteAll()
        val factory = repositoryDao.getAll()
        val list = (factory.create() as LimitOffsetDataSource).loadRange(0, listSize)
        assertThat(list.size, equalTo(0))
    }

    @Test
    fun insertRepositoriesInStarAscOrderAndRetrieveInDesc() {
        val listSize = 15
        val repositories = mockManyRepositoriesOrderedByStarAsc(listSize)
        repositoryDao.insertAll(*repositories)
        val factory = repositoryDao.getAll()
        val list = (factory.create() as LimitOffsetDataSource).loadRange(0, listSize)
        assertThat(list, equalTo(repositories.reversed()))
    }

    private fun mockManyRepositories(size: Int) =
        (1..size).map { position -> mockRepository(id = position) }
            .toTypedArray()

    @Suppress("SameParameterValue")
    private fun mockManyRepositoriesOrderedByStarAsc(size: Int) =
        (1..size).map { position -> mockRepository(id = position, stars = position) }
            .toTypedArray()

    private fun mockRepository(id: Int = 1, stars: Int = 0) =
        Repository(
            id = id,
            fullName = "Repository Name",
            avatarUrl = "https://wwww.avatar.com",
            author = "Repository Author",
            forks = 0,
            stars = stars
        )
}