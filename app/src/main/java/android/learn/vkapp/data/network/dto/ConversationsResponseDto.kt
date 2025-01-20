package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ConversationsResponseDto(
    @SerializedName("response") val conversations: ConversationsDto
)
