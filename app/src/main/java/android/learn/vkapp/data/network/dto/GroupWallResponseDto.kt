package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class GroupWallResponseDto(
    @SerializedName("response") val response: GroupWallDto
)
