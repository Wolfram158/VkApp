package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class GroupsDto(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<GroupDto>
)