package android.learn.vkapp.di

import android.learn.vkapp.data.network.ApiFactory
import android.learn.vkapp.data.repository.CommentsRepositoryImpl
import android.learn.vkapp.data.repository.GroupRepositoryImpl
import android.learn.vkapp.data.repository.GroupsRepositoryImpl
import android.learn.vkapp.data.repository.NewsFeedRepositoryImpl
import android.learn.vkapp.domain.comments.CommentsRepository
import android.learn.vkapp.domain.group.GroupRepository
import android.learn.vkapp.domain.groups.GroupsRepository
import android.learn.vkapp.domain.news.NewsFeedRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @Binds
    @ApplicationScope
    fun bindNewsFeedRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository

    @Binds
    @ApplicationScope
    fun bindGroupsRepository(impl: GroupsRepositoryImpl): GroupsRepository

    @Binds
    @ApplicationScope
    fun bindGroupRepository(impl: GroupRepositoryImpl): GroupRepository

    @Binds
    @ApplicationScope
    fun bindCommentsRepository(impl: CommentsRepositoryImpl): CommentsRepository

    companion object {
        @Provides
        @ApplicationScope
        fun provideApiService() = ApiFactory.apiService
    }
}