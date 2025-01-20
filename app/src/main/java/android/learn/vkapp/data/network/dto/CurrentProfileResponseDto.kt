package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class CurrentProfileResponseDto(
    @SerializedName("response") val profile: ProfileDto
)
