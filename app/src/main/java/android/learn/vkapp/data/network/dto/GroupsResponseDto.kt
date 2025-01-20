package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class GroupsResponseDto(
    @SerializedName("response") val groups: GroupsDto
)
