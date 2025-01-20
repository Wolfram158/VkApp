package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("likes") val likes: LikesInfoDto,
    @SerializedName("id") val id: String,
    @SerializedName("from_id") val fromId: String,
    @SerializedName("text") val text: String,
    @SerializedName("date") val date: String,
    @SerializedName("post_id") val postId: String,
    @SerializedName("owner_id") val ownerId: String
)
