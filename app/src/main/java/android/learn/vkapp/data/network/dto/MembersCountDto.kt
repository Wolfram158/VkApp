package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class MembersCountDto(
    @SerializedName("count") val count: Int
)
