package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class LikesCountDto(
    @SerializedName("likes") val count: Int,
)