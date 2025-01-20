package android.learn.vkapp.presentation.news

import android.learn.vkapp.data.network.dto.NewsFeedResponseDto

sealed class State

data object Error: State()

data object Progress: State()

data class Result(val result: NewsFeedResponseDto?) : State()