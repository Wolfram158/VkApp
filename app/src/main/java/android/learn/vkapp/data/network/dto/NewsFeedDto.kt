package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class NewsFeedDto(
    @SerializedName("items") val items: List<ItemFeedDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>,
    @SerializedName("groups") val groups: List<GroupDto>,
    @SerializedName("next_from") val nextFrom: String?
)
