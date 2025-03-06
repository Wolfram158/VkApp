package android.learn.vkapp.domain.group

import android.learn.vkapp.data.network.dto.LikesCountResponseDto
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun loadWall(id: String): Flow<PagingData<ItemWall>>

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