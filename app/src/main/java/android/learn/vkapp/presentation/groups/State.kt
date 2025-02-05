package android.learn.vkapp.presentation.groups

sealed class State

data object Initial : State()

data class Error<T>(val query: T) : State()

data class Progress<T>(val progress: T) : State()

data class Result<T>(val result: T) : State()