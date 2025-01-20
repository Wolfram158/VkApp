package android.learn.vkapp.presentation.groups

import android.learn.vkapp.data.network.ApiFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GroupsViewModel : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun loadGroups(token: String) {
        viewModelScope.launch {
            try {
                _state.value = Progress
                val response =
                    ApiFactory.apiService.loadGroups(token, "1", "members_count")
                _state.value = Result(response)
            } catch (_: Exception) {
                _state.value = Error
            }
        }
    }
}