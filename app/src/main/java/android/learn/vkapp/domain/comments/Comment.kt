package android.learn.vkapp.domain.comments

import android.learn.vkapp.domain.news.LikesInfo

data class Comment(
    val likes: LikesInfo,
    val id: String,
    val text: String,
    val date: String,
    val postId: String,
    val ownerId: String,
    val fromId: String,
    val firstName: String,
    val lastName: String,
    val photoUrl: String,
    val screenName: String
)
