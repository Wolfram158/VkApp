package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class CommentsDto(
    @SerializedName("items") val items: List<CommentDto>,
    @SerializedName("count") val count: String,
    @SerializedName("profiles") val profiles: List<ProfileDto>,
    @SerializedName("groups") val groups: List<GroupDto>
)
