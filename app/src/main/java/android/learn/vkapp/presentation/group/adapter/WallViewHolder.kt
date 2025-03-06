package android.learn.vkapp.presentation.group.adapter

import android.learn.vkapp.databinding.ItemFeedBinding
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView

class WallViewHolder(
    val binding: ItemFeedBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(loadState: LoadState) = with(binding) {
        if (loadState is LoadState.Error) {
            errorText.text = loadState.error.localizedMessage
        }
        progressBar.isVisible = loadState is LoadState.Loading
        tryLoadButton.isVisible = loadState is LoadState.Error
        errorText.isVisible = loadState is LoadState.Error
    }
}