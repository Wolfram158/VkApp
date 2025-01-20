package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("is_closed") val isClosed: String,
    @SerializedName("photo_200") val photo: String,
    @SerializedName("screen_name") val screenName: String,
    @SerializedName("members_count") val membersCount: String
)
