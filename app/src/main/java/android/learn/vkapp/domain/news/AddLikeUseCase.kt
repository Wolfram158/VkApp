package android.learn.vkapp.domain.news

import javax.inject.Inject

class AddLikeUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(
        token: String,
        type: String,
        itemId: Long,
        ownerId: Long
    ) = repository.addLike(token, type, itemId, ownerId)
}