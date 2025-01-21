package android.learn.vkapp.domain.comments

import javax.inject.Inject

class LoadCommentsUseCase @Inject constructor(
    private val repository: CommentsRepository
) {
    suspend operator fun invoke(
        token: String,
        ownerId: String,
        postId: String,
        needLikes: String,
        order: String,
        extended: String,
        extra: String
    ) = repository.loadComments(token, ownerId, postId, needLikes, order, extended, extra)
}