package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class LikesInfoDto(
    @SerializedName("count") val count: String,
    @SerializedName("user_likes") val wasLiked: String,
    @SerializedName("can_like") val canLike: String,
    @SerializedName("can_publish") val canPublish: String
)
