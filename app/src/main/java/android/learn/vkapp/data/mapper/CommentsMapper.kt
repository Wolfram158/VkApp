package android.learn.vkapp.data.mapper

import android.learn.vkapp.data.network.dto.CommentsResponseDto
import android.learn.vkapp.data.network.dto.LikesInfoDto
import android.learn.vkapp.domain.comments.Comment
import android.learn.vkapp.domain.news.LikesInfo
import kotlin.math.absoluteValue

class CommentsMapper {
    fun mapToComments(responseDto: CommentsResponseDto): List<Comment> {
        val result = mutableListOf<Comment>()

        val groups = responseDto.response.groups
        val profiles = responseDto.response.profiles
        val comments = responseDto.response.items

        for (comment in comments) {
            val group =
                groups.find { it1 ->
                    it1.id.toLong().absoluteValue == comment.fromId.toLong().absoluteValue
                }
            if (group != null) {
                val item = Comment(
                    id = comment.id,
                    text = comment.text,
                    date = comment.date,
                    likes = comment.likes.mapToLikesInfo(),
                    photoUrl = group.photo,
                    firstName = group.name,
                    lastName = "",
                    screenName = group.screenName,
                    fromId = group.id,
                    postId = comment.postId,
                    ownerId = comment.ownerId
                )
                result.add(item)
            } else {
                val profile =
                    profiles.find { it.id.toLong() == comment.fromId.toLong().absoluteValue }
                        ?: continue
                val item = Comment(
                    id = comment.id,
                    text = comment.text,
                    date = comment.date,
                    likes = comment.likes.mapToLikesInfo(),
                    photoUrl = profile.photoUrl,
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    screenName = profile.screenName,
                    fromId = profile.id,
                    postId = comment.postId,
                    ownerId = comment.ownerId
                )
                result.add(item)
            }
        }

        return result
    }

    private fun LikesInfoDto.mapToLikesInfo() = LikesInfo(
        count = count,
        wasLiked = wasLiked,
        canLike = canLike,
        canPublish = canPublish
    )
}