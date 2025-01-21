package android.learn.vkapp.presentation.group

import android.learn.vkapp.domain.group.LoadWallUseCase
import android.learn.vkapp.domain.group.AddLikeUseCase
import android.learn.vkapp.domain.group.DeleteLikeUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WallViewModel @Inject constructor(
    private val loadWallUseCase: LoadWallUseCase,
    private val addLikeUseCase: AddLikeUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase
) : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun loadWall(token: String, groupId: String) {
        _state.value = Progress
        viewModelScope.launch {
            try {
                val response = loadWallUseCase(token, "-$groupId", "1")
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