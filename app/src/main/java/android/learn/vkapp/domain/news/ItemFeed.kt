package android.learn.vkapp.domain.news

sealed class ItemFeedAdvanced

data class ItemFeed(
    val postId: String,
    val sourceId: String,
    val ownerName: String,
    val ownerId: String,
    val ownerPhotoUrl: String?,
    val photoContentUrl: String?,
    val id: String,
    val type: String,
    val date: String,
    val text: String,
    val views: String,
    val comments: CommentsInfo,
    val likes: LikesInfo,
    val reposts: RepostsInfo
) : ItemFeedAdvanced()

data object Loading : ItemFeedAdvanced()

data object Error : ItemFeedAdvanced()

data object RunOutOfNews : ItemFeedAdvanced()