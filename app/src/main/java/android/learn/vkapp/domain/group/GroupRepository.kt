package android.learn.vkapp.domain.group

import android.learn.vkapp.data.network.dto.GroupWallResponseDto
import android.learn.vkapp.data.network.dto.LikesCountResponseDto

interface GroupRepository {
    suspend fun loadWall(token: String, id: String, extended: String): GroupWallResponseDto

    suspend fun addLike(
        token: String,
        type: String,
        itemId: Long,
        ownerId: Long
    ): LikesCountResponseDto

    suspend fun deleteLike(
        token: String,
        type: String,
        itemId: Long,
        ownerId: Long
    ): LikesCountResponseDto
}