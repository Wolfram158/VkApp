package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ConversationDto(
    @SerializedName("peer") val peer: PeerDto,
    @SerializedName("unread_count") val unreadCount: Int,
    @SerializedName("chat_settings") val chatSettings: ChatSettingsDto
)
