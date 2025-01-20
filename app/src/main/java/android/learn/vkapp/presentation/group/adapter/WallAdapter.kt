package android.learn.vkapp.presentation.group.adapter

import android.graphics.Color
import android.learn.vkapp.databinding.ItemFeedBinding
import android.learn.vkapp.domain.group.ItemWall
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WallAdapter : ListAdapter<ItemWall, WallViewHolder>(WallDiffUtilCallback) {
    var onLikeClick: OnLikeClickListener? = null
    var onDislikeClick: OnDislikeClickListener? = null
    var onGotoCommentsClickListener: OnGotoCommentsClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallViewHolder {
        val binding = ItemFeedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WallViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WallViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            textNews.text = item.text
            textLike.text = item.likes.count
            textRepost.text = item.reposts.count
            textComments.text = item.comments.count
            textViews.text = item.views.toString()
            Picasso.get().load(item.ownerPhotoUrl).into(ownerNewsImage)
            ownerNewsName.text = item.ownerName
            Picasso.get().load(item.photoContentUrl).into(imageAttachment)
            val itemLong = item.date.toLong()
            val d = Date(itemLong * 1000L)
            val itemDateStr = SimpleDateFormat("yyyy dd MMM HH:mm", Locale.getDefault()).format(d)
            newsDate.text = itemDateStr
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
            imageComments.setOnClickListener {
                onGotoCommentsClickListener?.onGotoCommentsClick(item.id, item.ownerId)
            }
            textComments.setOnClickListener {
                onGotoCommentsClickListener?.onGotoCommentsClick(item.id, item.ownerId)
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
        fun onLikeClick(itemWall: ItemWall, position: Int)
    }

    interface OnDislikeClickListener {
        fun onDislikeClick(itemWall: ItemWall, position: Int)
    }

    interface OnGotoCommentsClickListener {
        fun onGotoCommentsClick(postId: String, ownerId: String)
    }
}