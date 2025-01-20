package android.learn.vkapp.presentation.group

import android.learn.vkapp.data.network.dto.GroupWallResponseDto

sealed class State

data object Error: State()

data object Progress: State()

data class Result(val result: GroupWallResponseDto?) : State()