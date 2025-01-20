package android.learn.vkapp.presentation.comments.adapter

import android.learn.vkapp.domain.comments.Comment
import androidx.recyclerview.widget.DiffUtil

object CommentsDiffUtilCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}