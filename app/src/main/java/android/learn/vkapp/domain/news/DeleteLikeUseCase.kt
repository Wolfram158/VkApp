package android.learn.vkapp.domain.news

import javax.inject.Inject

class DeleteLikeUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(
        token: String,
        type: String,
        itemId: Long,
        ownerId: Long
    ) = repository.deleteLike(token, type, itemId, ownerId)
}