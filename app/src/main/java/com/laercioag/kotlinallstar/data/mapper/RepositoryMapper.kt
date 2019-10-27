package com.laercioag.kotlinallstar.data.mapper

import com.laercioag.kotlinallstar.data.local.entity.Repository
import com.laercioag.kotlinallstar.data.remote.dto.Response
import javax.inject.Inject

interface RepositoryMapper {
    fun mapTo(response: Response): List<Repository>
}

class RepositoryMapperImpl @Inject constructor() : RepositoryMapper {

    override fun mapTo(response: Response): List<Repository> =
        response.items?.map { item ->
            with(item) {
                Repository(
                    id = id ?: 0,
                    fullName = fullName.orEmpty(),
                    author = owner?.login.orEmpty(),
                    avatarUrl = owner?.avatarUrl.orEmpty(),
                    stars = stargazersCount ?: 0,
                    forks = forksCount ?: 0
                )
            }
        }.orEmpty()
}