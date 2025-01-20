package android.learn.vkapp.presentation.news

import android.learn.vkapp.data.network.ApiFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun loadRecommendations(token: String) {
        _state.value = Progress
        viewModelScope.launch {
            try {
                val response = ApiFactory.apiService.loadRecommendations(token)
                _state.value = Result(response)
            } catch (_: Exception) {
                _state.value = Error
            }
        }
    }
}