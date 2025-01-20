package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("id") val id: Int,
    @SerializedName("peer_id") val peerId: Int,
    @SerializedName("date") val date: Int,
    @SerializedName("from_id") val fromId: Int,
    @SerializedName("text") val text: String,
    @SerializedName("members_count") val membersCount: Int
)
