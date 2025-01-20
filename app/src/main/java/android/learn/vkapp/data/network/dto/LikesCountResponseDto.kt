package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class LikesCountResponseDto(
    @SerializedName("response") val response: LikesCountDto
)
