package android.learn.vkapp.data.repository

import android.learn.vkapp.data.network.ApiService
import android.learn.vkapp.data.network.WallPageSource
import android.learn.vkapp.data.network.dto.LikesCountResponseDto
import android.learn.vkapp.domain.group.GroupRepository
import androidx.paging.Pager
import androidx.paging.PagingConfig
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : GroupRepository {
    override fun loadWall(
        id: String,
    ) = Pager(
        pagingSourceFactory = { WallPageSource(ownerId = id, apiService = apiService) },
        config = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 1)
    ).flow

    override suspend fun addLike(
        token: String,
        type: String,
        itemId: Long,
        ownerId: Long
    ): LikesCountResponseDto {
        return apiService.addLike(token, type, itemId, ownerId)
    }

    override suspend fun deleteLike(
        token: String,
        type: String,
        itemId: Long,
        ownerId: Long
    ): LikesCountResponseDto {
        return apiService.deleteLike(token, type, itemId, ownerId)
    }
}