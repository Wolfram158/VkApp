package android.learn.vkapp.presentation.comments

import android.learn.vkapp.data.network.ApiFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CommentsViewModel : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun loadComments(token: String, postId: String, ownerId: String) {
        viewModelScope.launch {
            try {
                _state.value = Progress
                val result = ApiFactory.apiService.loadComments(
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
}