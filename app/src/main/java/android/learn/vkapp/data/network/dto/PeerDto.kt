package android.learn.vkapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class PeerDto(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("local_id") val localId: Int
)
