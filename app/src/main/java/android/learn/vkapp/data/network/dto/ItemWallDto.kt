package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ItemWallDto(
    @SerializedName("id") val id: String,
    @SerializedName("owner_id") val ownerId: String,
    @SerializedName("from_id") val fromId: String,
    @SerializedName("date") val date: String,
    @SerializedName("text") val text: String,
    @SerializedName("comments") val comments: CommentsInfoDto,
    @SerializedName("likes") val likes: LikesInfoDto,
    @SerializedName("reposts") val reposts: RepostsInfoDto,
    @SerializedName("attachments") val attachment: List<AttachmentDto>,
    @SerializedName("views") val views: ViewsDto
)
