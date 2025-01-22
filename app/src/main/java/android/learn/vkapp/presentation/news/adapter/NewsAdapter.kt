package android.learn.vkapp.presentation.news.adapter

import android.graphics.Color
import android.learn.vkapp.R
import android.learn.vkapp.domain.news.Error
import android.learn.vkapp.domain.news.ItemFeed
import android.learn.vkapp.domain.news.ItemFeedAdvanced
import android.learn.vkapp.domain.news.Loading
import android.learn.vkapp.domain.news.RunOutOfNews
import android.learn.vkapp.utils.fromUnixToReadable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import java.lang.RuntimeException

class NewsAdapter : ListAdapter<ItemFeedAdvanced, NewsViewHolder>(NewsDiffUtilCallback) {
    var onLikeClick: OnLikeClickListener? = null
    var onDislikeClick: OnDislikeClickListener? = null
    var onGotoWallClickListener: OnGotoWallClickListener? = null
    var onGotoCommentsClickListener: OnGotoCommentsClickListener? = null
    var onTryLoadClickListener: OnTryLoadClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val layout = when (viewType) {
            NEWS_TYPE -> R.layout.item_feed

            RUN_OUT_OF_NEWS_TYPE -> R.layout.item_run_out_of

            LOADING_TYPE -> R.layout.item_loading

            ERROR_TYPE -> R.layout.item_error

            else -> throw RuntimeException("Unknown type of view")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ItemFeed -> {
                with(holder) {
                    textNews?.text = item.text
                    textLike?.text = item.likes.count
                    textRepost?.text = item.reposts.count
                    textComments?.text = item.comments.count
                    textViews?.text = item.views
                    Picasso.get().load(item.ownerPhotoUrl).into(ownerNewsImage)
                    ownerNewsName?.text = item.ownerName
                    Picasso.get().load(item.photoContentUrl).into(imageAttachment)
                    newsDate?.text = fromUnixToReadable(item.date.toLong())
                    if (item.likes.wasLiked == "1") {
                        imageLike?.setColorFilter(Color.RED)
                    } else {
                        imageLike?.setColorFilter(Color.BLACK)
                    }
                    imageLike?.setOnClickListener {
                        if (item.likes.wasLiked == "0") {
                            onLikeClick?.onLikeClick(item, position)
                        } else {
                            onDislikeClick?.onDislikeClick(item, position)
                        }
                    }
                    imageComments?.setOnClickListener {
                        onGotoCommentsClickListener?.onGotoCommentsClick(item.postId, item.ownerId)
                    }
                    textComments?.setOnClickListener {
                        onGotoCommentsClickListener?.onGotoCommentsClick(item.postId, item.ownerId)
                    }
                    ownerNewsName?.setOnClickListener {
                        onGotoWallClickListener?.onGotoWallClick(item.ownerId)
                    }
                    ownerNewsImage?.setOnClickListener {
                        onGotoWallClickListener?.onGotoWallClick(item.ownerId)
                    }
                }
            }

            Error -> {
                holder.tryLoadButton?.setOnClickListener {
                    onTryLoadClickListener?.onTryLoadClick()
                }
            }

            Loading -> {}
            RunOutOfNews -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            Error -> ERROR_TYPE
            is ItemFeed -> NEWS_TYPE
            Loading -> LOADING_TYPE
            RunOutOfNews -> RUN_OUT_OF_NEWS_TYPE
        }
    }

    fun updateLikes(likesCount: String, position: Int, isAdd: Boolean = true) {
        val currentList = currentList.toMutableList()
        currentList[position] = (currentList[position] as ItemFeed)
            .copy(
                likes = (currentList[position] as ItemFeed).likes.copy(
                    count = likesCount,
                    wasLiked = if (isAdd) "1" else "0"
                ),
            )
        submitList(currentList)
    }

    interface OnLikeClickListener {
        fun onLikeClick(itemFeed: ItemFeed, position: Int)
    }

    interface OnDislikeClickListener {
        fun onDislikeClick(itemFeed: ItemFeed, position: Int)
    }

    interface OnGotoWallClickListener {
        fun onGotoWallClick(id: String)
    }

    interface OnGotoCommentsClickListener {
        fun onGotoCommentsClick(postId: String, ownerId: String)
    }

    interface OnTryLoadClickListener {
        fun onTryLoadClick()
    }

    companion object {
        private const val NEWS_TYPE = 0
        private const val LOADING_TYPE = 1
        private const val ERROR_TYPE = 2
        private const val RUN_OUT_OF_NEWS_TYPE = 3
    }
}