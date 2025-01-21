package android.learn.vkapp.presentation.groups

import android.learn.vkapp.domain.groups.LoadGroupsUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroupsViewModel @Inject constructor(
    private val loadGroupsUseCase: LoadGroupsUseCase
) : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun loadGroups(token: String) {
        viewModelScope.launch {
            try {
                _state.value = Progress
                val response = loadGroupsUseCase(token)
                _state.value = Result(response)
            } catch (_: Exception) {
                _state.value = Error
            }
        }
    }
}