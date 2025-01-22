package android.learn.vkapp.presentation.news

sealed class State

data object Initial : State()

data object FirstError : State()

data object FirstProgress : State()

data object Error : State()

data object Progress : State()

data object Result : State()