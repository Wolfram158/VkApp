package android.learn.vkapp.presentation.news

import android.content.Context
import android.learn.vkapp.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.learn.vkapp.databinding.FragmentNewsBinding
import android.learn.vkapp.domain.news.ItemFeed
import android.learn.vkapp.presentation.App
import android.learn.vkapp.presentation.ViewModelFactory
import android.learn.vkapp.presentation.comments.CommentsFragment
import android.learn.vkapp.presentation.group.GroupFragment
import android.learn.vkapp.presentation.news.adapter.NewsAdapter
import android.learn.vkapp.utils.getAccessToken
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        newsViewModel.loadRecommendations(getAccessToken(), adapter)
    }

    private fun observeViewModel() {
        newsViewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
        newsViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                Result -> {
                    adapter.submitList(newsViewModel.getData())
                    adapter.notifyItemRangeInserted(
                        newsViewModel.getData().size,
                        newsViewModel.getPerPage()
                    )
                    binding.progressBar.visibility = GONE
                }

                FirstProgress -> {
                    binding.progressBar.visibility = VISIBLE
                }

                FirstError -> {
                    binding.progressBar.visibility = GONE
                    binding.errorText.visibility = VISIBLE
                    binding.tryLoadButton.visibility = VISIBLE
                }

                Error -> {
                    adapter.submitList(newsViewModel.getData())
                    adapter.notifyItemInserted(newsViewModel.getData().size - 1)
                }

                Progress -> {}
                Initial -> {}
            }
        }
    }

    private fun initAdapter() {
        adapter = NewsAdapter()
        binding.newsRv.adapter = adapter
        adapter.onLikeClick = object : NewsAdapter.OnLikeClickListener {
            override fun onLikeClick(itemFeed: ItemFeed, position: Int) {
                lifecycleScope.launch {
                    val response = newsViewModel.addLike(
                        getAccessToken(),
                        LIKE_OBJECT,
                        itemFeed.postId.toLong().absoluteValue,
                        itemFeed.sourceId.toLong()
                    )
                    response.response.count.let { adapter.updateLikes(it.toString(), position) }
                }
            }
        }
        adapter.onDislikeClick = object : NewsAdapter.OnDislikeClickListener {
            override fun onDislikeClick(itemFeed: ItemFeed, position: Int) {
                lifecycleScope.launch {
                    val response = newsViewModel.deleteLike(
                        getAccessToken(),
                        LIKE_OBJECT,
                        itemFeed.postId.toLong().absoluteValue,
                        itemFeed.sourceId.toLong()
                    )
                    response.response.count.let {
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
        adapter.onTryLoadClickListener = object : NewsAdapter.OnTryLoadClickListener {
            override fun onTryLoadClick() {
                newsViewModel.loadRecommendations(getAccessToken(), adapter)
            }
        }
        with(binding.newsRv) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    layoutManager?.let {
                        val visibleItemCount = it.childCount
                        val totalItemCount = it.itemCount
                        val firstVisibleItemPosition =
                            (layoutManager as LinearLayoutManager)
                                .findFirstVisibleItemPosition()
                        if (newsViewModel.state.value !is FirstProgress &&
                            newsViewModel.state.value !is Progress &&
                            newsViewModel.state.value !is Error &&
                            !newsViewModel.isAllLoaded()
                        ) {
                            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                                && firstVisibleItemPosition >= 0
                            ) {
                                newsViewModel.loadRecommendations(
                                    getAccessToken(),
                                    this@NewsFragment.adapter
                                )
                            }
                        }
                    }
                }
            })
        }
    }

    companion object {
        const val TAG = "NewsFragment"
        private const val LIKE_OBJECT = "post"

        fun newInstance() = NewsFragment()
    }
}