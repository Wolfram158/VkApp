package android.learn.vkapp.presentation.group

import android.learn.vkapp.data.network.ApiFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WallViewModel : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun loadWall(token: String, groupId: String) {
        _state.value = Progress
        viewModelScope.launch {
            try {
                val response = ApiFactory.apiService.loadWall(token, "-$groupId", "1")
                _state.value = Result(response)
            } catch (_: Exception) {
                _state.value = Error
            }
        }
    }
}