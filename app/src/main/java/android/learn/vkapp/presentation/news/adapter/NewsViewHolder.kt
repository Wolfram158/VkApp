package android.learn.vkapp.presentation.news.adapter

import android.learn.vkapp.R
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewsViewHolder(
    val view: View
) : RecyclerView.ViewHolder(view) {
    val textNews: TextView? = view.findViewById(R.id.text_news)
    val textLike: TextView? = view.findViewById(R.id.text_like)
    val textRepost: TextView? = view.findViewById(R.id.text_repost)
    val textComments: TextView? = view.findViewById(R.id.text_comments)
    val textViews: TextView? = view.findViewById(R.id.text_views)
    val newsDate: TextView? = view.findViewById(R.id.news_date)
    val ownerNewsName: TextView? = view.findViewById(R.id.owner_news_name)
    val ownerNewsImage: ImageView? = view.findViewById(R.id.owner_news_image)
    val imageAttachment: ImageView? = view.findViewById(R.id.image_attachment)
    val imageLike: ImageView? = view.findViewById(R.id.image_like)
    val imageComments: ImageView? = view.findViewById(R.id.image_comments)
    val tryLoadButton: Button? = view.findViewById(R.id.try_load_button)
}
