package android.learn.vkapp.presentation.group.adapter

import android.learn.vkapp.domain.group.ItemWall
import android.learn.vkapp.domain.news.ItemFeed
import androidx.recyclerview.widget.DiffUtil

object WallDiffUtilCallback : DiffUtil.ItemCallback<ItemWall>() {
    override fun areItemsTheSame(oldItem: ItemWall, newItem: ItemWall): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemWall, newItem: ItemWall): Boolean {
        return oldItem == newItem
    }

}