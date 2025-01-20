package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class GroupWallDto(
    @SerializedName("count") val count: String,
    @SerializedName("items") val items: List<ItemWallDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>,
    @SerializedName("groups") val groups: List<GroupDto>
)
