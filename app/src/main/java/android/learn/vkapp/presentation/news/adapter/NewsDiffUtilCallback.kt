package android.learn.vkapp.presentation.news.adapter

import android.learn.vkapp.domain.news.ItemFeed
import androidx.recyclerview.widget.DiffUtil

object NewsDiffUtilCallback : DiffUtil.ItemCallback<ItemFeed>() {
    override fun areItemsTheSame(oldItem: ItemFeed, newItem: ItemFeed): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemFeed, newItem: ItemFeed): Boolean {
        return oldItem == newItem
    }

}