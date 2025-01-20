package android.learn.vkapp.presentation.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.learn.vkapp.R
import android.learn.vkapp.data.mapper.ItemFeedMapper
import android.learn.vkapp.data.network.ApiFactory
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.vk.id.VKID
import kotlinx.coroutines.launch

class MessagesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            Log.d("VK", VKID.instance.accessToken?.token.toString())
            val response = VKID.instance.accessToken?.token?.let {
                ApiFactory.apiService.loadConversations(it, "all")
            }
//            Log.d("VK", response.toString())
//            response?.let {
//                adapter.submitList(
//                    ItemFeedMapper().mapToItemFeed(it)
//                )
//            }
        }
    }

    companion object {
        fun newInstance() = MessagesFragment()
    }
}