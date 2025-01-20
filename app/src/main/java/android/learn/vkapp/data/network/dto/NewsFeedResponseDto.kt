package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class NewsFeedResponseDto(
    @SerializedName("response") val response: NewsFeedDto
)
