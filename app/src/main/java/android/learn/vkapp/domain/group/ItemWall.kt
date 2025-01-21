package android.learn.vkapp.domain.group

import android.learn.vkapp.domain.news.CommentsInfo
import android.learn.vkapp.domain.news.LikesInfo
import android.learn.vkapp.domain.news.RepostsInfo

data class ItemWall(
    val id: String,
    val ownerId: String,
    val ownerName: String,
    val fromId: String,
    val date: String,
    val text: String,
    val comments: CommentsInfo,
    val likes: LikesInfo,
    val reposts: RepostsInfo,
    val ownerPhotoUrl: String?,
    val photoContentUrl: String?,
    val views: Int
)