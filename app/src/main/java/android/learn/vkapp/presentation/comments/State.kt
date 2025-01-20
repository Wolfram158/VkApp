package android.learn.vkapp.presentation.comments

import android.learn.vkapp.data.network.dto.CommentsResponseDto

sealed class State

data object Error: State()

data object Progress: State()

data class Result(val result: CommentsResponseDto?) : State()