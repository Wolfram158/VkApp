package android.learn.vkapp.presentation.groups

import android.learn.vkapp.data.network.ApiFactory.apiService
import android.learn.vkapp.utils.getAccessToken
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FoundGroupsViewModel : ViewModel() {
    private val _groups = MutableStateFlow<State>(Initial)
    val groups: StateFlow<State> = _groups.asStateFlow()

    fun changeQuery(query: String) {
        viewModelScope.launch {
            _groups.emit(Progress(query))
        }
    }

    suspend fun loadGroups(query: String) {
        try {
            val res = apiService.searchGroups(
                getAccessToken(),
                query,
                "group",
                0,
                0,
                50,
                "members_count",
                "1"
            )
            _groups.emit(Result(res))
        } catch (_: Exception) {
            _groups.emit(Error(query))
        }
    }

    @OptIn(FlowPreview::class)
    fun observe(timeMillis: Long, scope: CoroutineScope, block: suspend (State) -> Unit) {
        _groups.debounce(timeMillis).onEach {
            block(it)
        }.launchIn(scope)
    }

}