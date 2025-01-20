package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class MembersCountResponseDto(
    @SerializedName("error") val error: Any?,
    @SerializedName("response") val membersCountDto: MembersCountDto
)
