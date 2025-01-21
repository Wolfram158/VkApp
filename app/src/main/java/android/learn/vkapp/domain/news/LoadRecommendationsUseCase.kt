package android.learn.vkapp.domain.news

import javax.inject.Inject

class LoadRecommendationsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(token: String) = repository.loadRecommendations(token)
}