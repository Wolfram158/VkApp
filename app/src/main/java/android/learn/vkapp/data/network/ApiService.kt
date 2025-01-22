package android.learn.vkapp.data.network

import android.learn.vkapp.data.network.dto.CommentsResponseDto
import android.learn.vkapp.data.network.dto.ConversationsResponseDto
import android.learn.vkapp.data.network.dto.GroupWallResponseDto
import android.learn.vkapp.data.network.dto.GroupsResponseDto
import android.learn.vkapp.data.network.dto.LikesCountResponseDto
import android.learn.vkapp.data.network.dto.NewsFeedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("newsfeed.getRecommended?v=5.199")
    suspend fun loadRecommendations(
        @Query("access_token") token: String,
        @Query("count") count: String
    ): NewsFeedResponseDto

    @GET("newsfeed.getRecommended?v=5.199")
    suspend fun loadRecommendations(
        @Query("access_token") token: String,
        @Query("start_from") startFrom: String,
        @Query("count") count: String
    ): NewsFeedResponseDto

    @GET("likes.add?v=5.199")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("type") type: String,
        @Query("item_id") itemId: Long,
        @Query("owner_id") ownerId: Long,
    ): LikesCountResponseDto

    @GET("likes.delete?v=5.199")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("type") type: String,
        @Query("item_id") itemId: Long,
        @Query("owner_id") ownerId: Long,
    ): LikesCountResponseDto

    @GET("messages.getConversations?v=5.199")
    suspend fun loadConversations(
        @Query("access_token") token: String,
        @Query("filter") filter: String
    ): ConversationsResponseDto

    @GET("wall.get?v=5.199")
    suspend fun loadWall(
        @Query("access_token") token: String,
        @Query("owner_id") id: String,
        @Query("extended") extended: String
    ): GroupWallResponseDto

    @GET("groups.get?v=5.199")
    suspend fun loadGroups(
        @Query("access_token") token: String,
        @Query("extended") extended: String,
        @Query("fields") extra: String
    ): GroupsResponseDto

//    @GET("groups.getMembers?v=5.199")
//    suspend fun getCountOfGroupMembers(
//        @Query("access_token") token: String,
//        @Query("group_id") groupId: String
//    ): MembersCountResponseDto

    @GET("wall.getComments?v=5.199")
    suspend fun loadComments(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: String,
        @Query("post_id") postId: String,
        @Query("need_likes") needLikes: String,
        @Query("sort") order: String,
        @Query("extended") extended: String,
        @Query("fields") extra: String
    ): CommentsResponseDto

//    @GET("users.get?v=5.199")
//    suspend fun getCurrentProfile(
//        @Query("access_token") token: String,
//    ): CurrentProfileResponseDto
}