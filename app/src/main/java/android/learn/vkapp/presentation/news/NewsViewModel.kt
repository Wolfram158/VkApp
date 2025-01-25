package android.learn.vkapp.presentation.news

import android.learn.vkapp.data.mapper.ItemFeedMapper
import android.learn.vkapp.data.network.dto.NewsFeedResponseDto
import android.learn.vkapp.domain.news.AddLikeUseCase
import android.learn.vkapp.domain.news.DeleteLikeUseCase
import android.learn.vkapp.domain.news.ItemFeedAdvanced
import android.learn.vkapp.domain.news.LoadNextRecommendationsUseCase
import android.learn.vkapp.domain.news.LoadRecommendationsUseCase
import android.learn.vkapp.domain.news.Loading
import android.learn.vkapp.domain.news.RunOutOfNews
import android.learn.vkapp.presentation.news.adapter.NewsViewHolder
import android.learn.vkapp.domain.news.Error as Err
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val addLikeUseCase: AddLikeUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase,
    private val loadRecommendationsUseCase: LoadRecommendationsUseCase,
    private val loadNextRecommendationsUseCase: LoadNextRecommendationsUseCase,
    private val mapper: ItemFeedMapper
) : ViewModel() {
    private val _state = MutableLiveData<State>(Initial)
    val state: LiveData<State>
        get() = _state

    private val _data = mutableListOf<ItemFeedAdvanced>()
    val data: List<ItemFeedAdvanced>
        get() = _data

    private var nextFrom: String? = null

    private var _isAllLoaded = false
    val isAllLoaded: Boolean
        get() = _isAllLoaded

    fun loadRecommendations(token: String, adapter: ListAdapter<ItemFeedAdvanced, NewsViewHolder>) {
        if (nextFrom == null && _state.value is Result) {
            _data.add(RunOutOfNews)
            adapter.submitList(_data)
            adapter.notifyItemInserted(_data.size - 1)
            _isAllLoaded = true
            return
        }
        if (_state.value == Initial || _data.size == 0) {
            _state.value = FirstProgress
        } else {
            _state.value = Progress
            if (_data.last() is Err) {
                _data.removeLast()
                adapter.submitList(_data)
                adapter.notifyItemRemoved(_data.size)
            }
            _data.add(Loading)
            adapter.submitList(_data)
            adapter.notifyItemInserted(_data.size)
        }
        var case = ErrorCase.NONE
        viewModelScope.launch {
            try {
                val nextFromVal = nextFrom
                val response: NewsFeedResponseDto = if (nextFromVal == null) {
                    case = ErrorCase.FIRST
                    loadRecommendationsUseCase(token, PER_PAGE_STRING)
                } else {
                    case = ErrorCase.NEXT
                    loadNextRecommendationsUseCase(token, nextFromVal, PER_PAGE_STRING)
                }
                val mapped = mapper.mapToItemFeed(response)
                nextFrom = response.response.nextFrom
                if (case == ErrorCase.NEXT) {
                    _data.removeLast()
                    adapter.submitList(_data)
                    adapter.notifyItemRemoved(_data.size)
                }
                _data.addAll(mapped)
                _state.value = Result
            } catch (_: Exception) {
                if (case == ErrorCase.FIRST) {
                    _state.value = FirstError
                } else {
                    _data.removeLast()
                    adapter.submitList(_data)
                    adapter.notifyItemRemoved(_data.size)
                    _data.add(Err)
                    _state.value = Error
                }
            }
        }
    }

    companion object {
        private const val PER_PAGE = 10
        private const val PER_PAGE_STRING = PER_PAGE.toString()
    }

    fun getPerPage() = PER_PAGE

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

enum class ErrorCase {
    NONE, FIRST, NEXT
}