package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ConversationAdvancedDto(
    @SerializedName("conversation") val conversation: ConversationDto,
    @SerializedName("last_message") val lastMessage: MessageDto
)
