package android.learn.vkapp.presentation.group.adapter

import android.learn.vkapp.databinding.ItemFeedBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class LoadStateAdapter : LoadStateAdapter<WallViewHolder>() {
    override fun onBindViewHolder(holder: WallViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): WallViewHolder =
        WallViewHolder(
            ItemFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}