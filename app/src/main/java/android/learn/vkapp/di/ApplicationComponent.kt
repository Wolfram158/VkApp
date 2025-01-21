package android.learn.vkapp.di

import android.learn.vkapp.presentation.App
import android.learn.vkapp.presentation.comments.CommentsFragment
import android.learn.vkapp.presentation.group.GroupFragment
import android.learn.vkapp.presentation.groups.GroupsFragment
import android.learn.vkapp.presentation.news.NewsFragment
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(app: App)

    fun inject(fragment: NewsFragment)

    fun inject(fragment: GroupsFragment)

    fun inject(fragment: GroupFragment)

    fun inject(fragment: CommentsFragment)
}