package android.learn.vkapp.presentation.groups

import android.learn.vkapp.data.network.dto.GroupsResponseDto

sealed class State

data object Error : State()

data object Progress : State()

data class Result(val result: GroupsResponseDto) : State()