package android.learn.vkapp.presentation.news

import android.learn.vkapp.domain.news.AddLikeUseCase
import android.learn.vkapp.domain.news.DeleteLikeUseCase
import android.learn.vkapp.domain.news.LoadRecommendationsUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val addLikeUseCase: AddLikeUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase,
    private val loadRecommendationsUseCase: LoadRecommendationsUseCase
) : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun loadRecommendations(token: String) {
        _state.value = Progress
        viewModelScope.launch {
            try {
                val response = loadRecommendationsUseCase(token)
                _state.value = Result(response)
            } catch (_: Exception) {
                _state.value = Error
            }
        }
    }

    suspend fun addLike(
        token: String,
        type: String,
        itemId: Long,
        ownerId: Long
    ) = addLikeUseCase(token, type, itemId, ownerId)

    suspend fun deleteLike(
        token: String,
        type: String,
        itemId: Long,
        ownerId: Long
    ) = deleteLikeUseCase(token, type, itemId, ownerId)
}