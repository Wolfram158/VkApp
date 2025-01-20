package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class CommentsInfoDto(
    @SerializedName("count") val count: String,
    @SerializedName("can_post") val canPost: String
)
