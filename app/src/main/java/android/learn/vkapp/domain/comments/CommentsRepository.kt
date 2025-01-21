package android.learn.vkapp.domain.comments

import android.learn.vkapp.data.network.dto.CommentsResponseDto
import android.learn.vkapp.data.network.dto.LikesCountResponseDto

interface CommentsRepository {
    suspend fun loadComments(
        token: String,
        ownerId: String,
        postId: String,
        needLikes: String,
        order: String,
        extended: String,
        extra: String
    ): CommentsResponseDto

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