package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("id") val id: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("photo_100") val photoUrl: String,
    @SerializedName("screen_name") val screenName: String
)
