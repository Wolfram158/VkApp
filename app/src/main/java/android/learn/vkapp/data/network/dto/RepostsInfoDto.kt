package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class RepostsInfoDto(
    @SerializedName("count") val count: String,
    @SerializedName("user_reposted") val wasReposted: String
)
