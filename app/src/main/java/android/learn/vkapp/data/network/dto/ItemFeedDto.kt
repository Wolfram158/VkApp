package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ItemFeedDto(
    @SerializedName("post_id") val postId: String,
    @SerializedName("source_id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("date") val date: String,
    @SerializedName("text") val text: String,
    @SerializedName("comments") val comments: CommentsInfoDto,
    @SerializedName("likes") val likes: LikesInfoDto,
    @SerializedName("reposts") val reposts: RepostsInfoDto,
    @SerializedName("attachments") val attachment: List<AttachmentDto>,
    @SerializedName("views") val views: ViewsDto
)
