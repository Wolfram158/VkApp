package android.learn.vkapp.presentation.comments

import android.learn.vkapp.domain.comments.LoadCommentsUseCase
import android.learn.vkapp.domain.group.AddLikeUseCase
import android.learn.vkapp.domain.group.DeleteLikeUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val loadCommentsUseCase: LoadCommentsUseCase,
    private val addLikeUseCase: AddLikeUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase
) : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun loadComments(token: String, postId: String, ownerId: String) {
        viewModelScope.launch {
            try {
                _state.value = Progress
                val result = loadCommentsUseCase(
                    token = token,
                    ownerId = ownerId,
                    postId = postId,
                    needLikes = "1",
                    order = "asc",
                    extended = "1",
                    extra = "photo_100,photo_200,screen_name"
                )
                _state.value = Result(result)
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