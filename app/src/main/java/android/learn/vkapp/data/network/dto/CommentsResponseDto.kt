package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class CommentsResponseDto(
    @SerializedName("response") val response: CommentsDto
)
