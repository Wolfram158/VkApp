package android.learn.vkapp.domain.comments

import javax.inject.Inject

class AddLikeUseCase @Inject constructor(
    private val repository: CommentsRepository
) {
    suspend operator fun invoke(
        token: String,
        type: String,
        itemId: Long,
        ownerId: Long
    ) = repository.addLike(token, type, itemId, ownerId)
}