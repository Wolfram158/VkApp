package android.learn.vkapp.data.mapper

import android.learn.vkapp.data.network.dto.CommentsInfoDto
import android.learn.vkapp.data.network.dto.GroupWallResponseDto
import android.learn.vkapp.data.network.dto.LikesInfoDto
import android.learn.vkapp.data.network.dto.RepostsInfoDto
import android.learn.vkapp.domain.group.ItemWall
import android.learn.vkapp.domain.news.CommentsInfo
import android.learn.vkapp.domain.news.LikesInfo
import android.learn.vkapp.domain.news.RepostsInfo
import kotlin.math.absoluteValue

class ItemWallMapper {
    fun mapToItemWall(responseDto: GroupWallResponseDto): List<ItemWall> {
        val result = mutableListOf<ItemWall>()

        val groups = responseDto.response.groups
        val profiles = responseDto.response.profiles
        val posts = responseDto.response.items
        for (post in posts.stream()
            .sorted(Comparator { o1, o2 -> return@Comparator if (o1.date < o2.date) 1 else -1 })) {
            val group = groups.find { it.id.toLong() == post.ownerId.toLong().absoluteValue } ?: continue
            val wallItem = ItemWall(
                ownerPhotoUrl = group.photo,
                ownerId = group.id,
                ownerName = group.name,
                id = post.id,
                text = post.text,
                date = post.date,
                views = post.views.count.toInt(),
                comments = post.comments.mapToCommentsInfo(),
                likes = post.likes.mapToLikesInfo(),
                reposts = post.reposts.mapToRepostsInfo(),
                photoContentUrl = post.attachment.lastOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                fromId = post.fromId
            )
            result.add(wallItem)
        }

        return result
    }

    private fun LikesInfoDto.mapToLikesInfo() = LikesInfo(
        count = count,
        wasLiked = wasLiked,
        canLike = canLike,
        canPublish = canPublish
    )

    private fun CommentsInfoDto.mapToCommentsInfo() = CommentsInfo(
        count = count,
        canPost = canPost
    )

    private fun RepostsInfoDto.mapToRepostsInfo() = RepostsInfo(
        count = count,
        wasReposted = wasReposted
    )
}

