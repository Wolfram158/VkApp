package android.learn.vkapp.domain.news

import android.learn.vkapp.data.network.dto.LikesCountResponseDto
import android.learn.vkapp.data.network.dto.NewsFeedResponseDto

interface NewsFeedRepository {
    suspend fun loadRecommendations(token: String, count: String): NewsFeedResponseDto

    suspend fun loadNextRecommendations(
        token: String,
        startFrom: String,
        count: String
    ): NewsFeedResponseDto

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