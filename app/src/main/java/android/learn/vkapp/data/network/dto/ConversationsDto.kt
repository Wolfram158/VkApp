package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ConversationsDto(
    @SerializedName("count") val count: Int,
    @SerializedName("unread_count") val unreadCount: Int,
    @SerializedName("profiles") val profiles: List<ProfileDto>,
    @SerializedName("groups") val groups: List<GroupDto>,
    @SerializedName("items") val conversations: List<ConversationAdvancedDto>
)
