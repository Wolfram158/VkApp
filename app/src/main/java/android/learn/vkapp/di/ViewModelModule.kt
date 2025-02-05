package android.learn.vkapp.di

import android.learn.vkapp.presentation.comments.CommentsViewModel
import android.learn.vkapp.presentation.group.WallViewModel
import android.learn.vkapp.presentation.groups.UserGroupsViewModel
import android.learn.vkapp.presentation.news.NewsViewModel
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    fun bindCommentsViewModel(viewModel: CommentsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WallViewModel::class)
    fun bindWallViewModel(viewModel: WallViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserGroupsViewModel::class)
    fun bindGroupsViewModel(viewModel: UserGroupsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel
}