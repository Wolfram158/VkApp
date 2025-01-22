package android.learn.vkapp.presentation.news.adapter

import android.learn.vkapp.domain.news.Error
import android.learn.vkapp.domain.news.ItemFeed
import android.learn.vkapp.domain.news.ItemFeedAdvanced
import android.learn.vkapp.domain.news.Loading
import android.learn.vkapp.domain.news.RunOutOfNews
import androidx.recyclerview.widget.DiffUtil

object NewsDiffUtilCallback : DiffUtil.ItemCallback<ItemFeedAdvanced>() {
    override fun areItemsTheSame(oldItem: ItemFeedAdvanced, newItem: ItemFeedAdvanced): Boolean {
        return when (oldItem) {
            Error -> newItem is Error
            is ItemFeed -> oldItem == newItem
            Loading -> newItem is Loading
            RunOutOfNews -> newItem is RunOutOfNews
        }
    }

    override fun areContentsTheSame(oldItem: ItemFeedAdvanced, newItem: ItemFeedAdvanced): Boolean {
        return oldItem == newItem
    }

}