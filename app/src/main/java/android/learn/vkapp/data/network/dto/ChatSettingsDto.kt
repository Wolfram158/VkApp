package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ChatSettingsDto(
    @SerializedName("members_count") val membersCount: Int,
    @SerializedName("title") val title: String,
    @SerializedName("state") val state: String,
    @SerializedName("photo") val photo: PhotoChatSettingsDto
)
