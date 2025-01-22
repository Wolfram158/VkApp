package android.learn.vkapp.data.repository

import android.learn.vkapp.data.network.ApiFactory.apiService
import android.learn.vkapp.data.network.ApiService
import android.learn.vkapp.data.network.dto.LikesCountResponseDto
import android.learn.vkapp.data.network.dto.NewsFeedResponseDto
import android.learn.vkapp.domain.news.NewsFeedRepository
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    apiService: ApiService
) : NewsFeedRepository {
    override suspend fun loadRecommendations(token: String, count: String): NewsFeedResponseDto {
        return apiService.loadRecommendations(token, count)
    }

    override suspend fun loadNextRecommendations(
        token: String,
        startFrom: String,
        count: String
    ): NewsFeedResponseDto {
        return apiService.loadRecommendations(token, startFrom, count)
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