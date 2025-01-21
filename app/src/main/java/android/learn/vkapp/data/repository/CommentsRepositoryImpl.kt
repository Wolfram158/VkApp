package android.learn.vkapp.data.repository

import android.learn.vkapp.data.network.ApiService
import android.learn.vkapp.data.network.dto.CommentsResponseDto
import android.learn.vkapp.data.network.dto.LikesCountResponseDto
import android.learn.vkapp.domain.comments.CommentsRepository
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CommentsRepository {
    override suspend fun loadComments(
        token: String,
        ownerId: String,
        postId: String,
        needLikes: String,
        order: String,
        extended: String,
        extra: String
    ): CommentsResponseDto {
        return apiService.loadComments(
            token = token,
            ownerId = ownerId,
            postId = postId,
            needLikes = "1",
            order = "asc",
            extended = "1",
            extra = "photo_100,photo_200,screen_name"
        )
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