package android.learn.vkapp.domain.news

import javax.inject.Inject

class LoadNextRecommendationsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(token: String, startFrom: String, count: String) =
        repository.loadNextRecommendations(token, startFrom, count)
}

