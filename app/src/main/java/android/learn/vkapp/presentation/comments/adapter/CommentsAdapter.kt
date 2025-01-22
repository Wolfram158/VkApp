package android.learn.vkapp.presentation.comments.adapter

import android.graphics.Color
import android.learn.vkapp.databinding.ItemCommentBinding
import android.learn.vkapp.domain.comments.Comment
import android.learn.vkapp.utils.fromUnixToReadable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso

class CommentsAdapter : ListAdapter<Comment, CommentsViewHolder>(CommentsDiffUtilCallback) {
    var onLikeClick: OnLikeClickListener? = null
    var onDislikeClick: OnDislikeClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            textLike.text = item.likes.count
            textComment.text = item.text
            val name = "${item.firstName} ${item.lastName}"
            ownerCommentName.text = name
            Picasso.get().load(item.photoUrl).into(ownerCommentImage)
            commentDate.text = fromUnixToReadable(item.date.toLong())
            if (item.likes.wasLiked == "1") {
                imageLike.setColorFilter(Color.RED)
            } else {
                imageLike.setColorFilter(Color.BLACK)
            }
            imageLike.setOnClickListener {
                if (item.likes.wasLiked == "0") {
                    onLikeClick?.onLikeClick(item, position)
                } else {
                    onDislikeClick?.onDislikeClick(item, position)
                }
            }
        }
    }

    fun updateLikes(likesCount: String, position: Int, isAdd: Boolean = true) {
        val currentList = currentList.toMutableList()
        currentList[position] = currentList[position]
            .copy(
                likes = currentList[position].likes.copy(
                    count = likesCount,
                    wasLiked = if (isAdd) "1" else "0"
                ),
            )
        submitList(currentList)
    }

    interface OnLikeClickListener {
        fun onLikeClick(comment: Comment, position: Int)
    }

    interface OnDislikeClickListener {
        fun onDislikeClick(comment: Comment, position: Int)
    }
}