package android.learn.vkapp.data.mapper

import android.learn.vkapp.data.network.dto.CommentsInfoDto
import android.learn.vkapp.data.network.dto.LikesInfoDto
import android.learn.vkapp.data.network.dto.NewsFeedResponseDto
import android.learn.vkapp.data.network.dto.RepostsInfoDto
import android.learn.vkapp.domain.news.CommentsInfo
import android.learn.vkapp.domain.news.ItemFeed
import android.learn.vkapp.domain.news.LikesInfo
import android.learn.vkapp.domain.news.RepostsInfo
import android.util.Log
import javax.inject.Inject
import kotlin.math.absoluteValue

class ItemFeedMapper @Inject constructor() {
    fun mapToItemFeed(responseDto: NewsFeedResponseDto): List<ItemFeed> {
        val result = mutableListOf<ItemFeed>()

        val groups = responseDto.response.groups
        val profiles = responseDto.response.profiles
        val posts = responseDto.response.items

        for (post in posts.stream()
            .sorted(Comparator { o1, o2 -> return@Comparator if (o1.date < o2.date) 1 else -1 })) {
            val group = groups.find { it.id.toLong() == post.id.toLong().absoluteValue } ?: continue
            val feedItem = ItemFeed(
                postId = post.postId,
                sourceId = post.id,
                ownerName = group.name,
                ownerPhotoUrl = group.photo,
                ownerId = group.id,
                id = post.id,
                text = post.text,
                type = post.type,
                date = post.date,
                views = post.views.count,
                comments = post.comments.mapToCommentsInfo(),
                likes = post.likes.mapToLikesInfo(),
                reposts = post.reposts.mapToRepostsInfo(),
                photoContentUrl = post.attachment.lastOrNull()?.photo?.photoUrls?.lastOrNull()?.url
            )
            result.add(feedItem)
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