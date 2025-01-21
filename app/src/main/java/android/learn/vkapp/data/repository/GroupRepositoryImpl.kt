package android.learn.vkapp.data.repository

import android.learn.vkapp.data.network.ApiService
import android.learn.vkapp.data.network.dto.GroupWallResponseDto
import android.learn.vkapp.data.network.dto.LikesCountResponseDto
import android.learn.vkapp.domain.group.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : GroupRepository {
    override suspend fun loadWall(
        token: String,
        id: String,
        extended: String
    ): GroupWallResponseDto {
        return apiService.loadWall(token, id, extended)
    }

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