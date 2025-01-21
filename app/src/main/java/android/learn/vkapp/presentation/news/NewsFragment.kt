package android.learn.vkapp.presentation.news

import android.content.Context
import android.learn.vkapp.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.learn.vkapp.data.mapper.ItemFeedMapper
import android.learn.vkapp.databinding.FragmentNewsBinding
import android.learn.vkapp.domain.news.ItemFeed
import android.learn.vkapp.presentation.App
import android.learn.vkapp.presentation.ViewModelFactory
import android.learn.vkapp.presentation.comments.CommentsFragment
import android.learn.vkapp.presentation.group.GroupFragment
import android.learn.vkapp.presentation.news.adapter.NewsAdapter
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.vk.id.VKID
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.math.absoluteValue

class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding: FragmentNewsBinding
        get() = _binding ?: throw RuntimeException("FragmentNewsBinding is null")

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as App).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        binding.tryLoadButton.setOnClickListener {
            binding.errorText.visibility = GONE
            binding.tryLoadButton.visibility = GONE
            loadRecommendations()
        }

        observeViewModel()

        loadRecommendations()
    }

    private fun loadRecommendations() {
        VKID.instance.accessToken?.token?.let {
            newsViewModel.loadRecommendations(it)
        }
    }

    private fun observeViewModel() {
        newsViewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
        newsViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                Error -> {
                    binding.progressBar.visibility = GONE
                    binding.errorText.visibility = VISIBLE
                    binding.tryLoadButton.visibility = VISIBLE
                }

                Progress -> binding.progressBar.visibility = VISIBLE
                is Result -> {
                    if (it.result != null) {
                        adapter.submitList(
                            ItemFeedMapper().mapToItemFeed(it.result)
                        )
                    }
                    binding.progressBar.visibility = GONE
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = NewsAdapter()
        binding.newsRv.adapter = adapter
        adapter.onLikeClick = object : NewsAdapter.OnLikeClickListener {
            override fun onLikeClick(itemFeed: ItemFeed, position: Int) {
                lifecycleScope.launch {
                    val response = VKID.instance.accessToken?.token?.let {
                        newsViewModel.addLike(
                            it,
                            "post",
                            itemFeed.postId.toLong().absoluteValue,
                            itemFeed.sourceId.toLong()
                        )
                    }
                    response?.response?.count?.let { adapter.updateLikes(it.toString(), position) }
                }
            }
        }
        adapter.onDislikeClick = object : NewsAdapter.OnDislikeClickListener {
            override fun onDislikeClick(itemFeed: ItemFeed, position: Int) {
                lifecycleScope.launch {
                    val response = VKID.instance.accessToken?.token?.let {
                        newsViewModel.deleteLike(
                            it,
                            "post",
                            itemFeed.postId.toLong().absoluteValue,
                            itemFeed.sourceId.toLong()
                        )
                    }
                    response?.response?.count?.let {
                        adapter.updateLikes(
                            it.toString(),
                            position,
                            false
                        )
                    }
                }
            }
        }
        adapter.onGotoWallClickListener = object : NewsAdapter.OnGotoWallClickListener {
            override fun onGotoWallClick(id: String) {
                parentFragmentManager.beginTransaction()
                    .add(R.id.home_container, GroupFragment.newInstance(id), null)
                    .hide(parentFragmentManager.fragments.last())
                    .addToBackStack(null).commit()
            }
        }
        adapter.onGotoCommentsClickListener = object : NewsAdapter.OnGotoCommentsClickListener {
            override fun onGotoCommentsClick(postId: String, ownerId: String) {
                parentFragmentManager.beginTransaction()
                    .add(
                        R.id.home_container,
                        CommentsFragment.newInstance(postId = postId, ownerId = ownerId),
                        null
                    )
                    .hide(parentFragmentManager.fragments.last())
                    .addToBackStack(null).commit()
            }
        }
    }

    companion object {
        const val TAG = "NewsFragment"

        fun newInstance() = NewsFragment()
    }
}