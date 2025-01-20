package android.learn.vkapp.presentation.groups.adapter

import android.learn.vkapp.domain.groups.Group
import androidx.recyclerview.widget.DiffUtil

object GroupsDiffUtilCallback : DiffUtil.ItemCallback<Group>() {
    override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem == newItem
    }
}