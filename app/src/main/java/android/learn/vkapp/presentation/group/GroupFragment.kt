package android.learn.vkapp.presentation.group

import android.content.Context
import android.learn.vkapp.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.learn.vkapp.databinding.FragmentGroupBinding
import android.learn.vkapp.domain.group.ItemWall
import android.learn.vkapp.presentation.App
import android.learn.vkapp.presentation.ViewModelFactory
import android.learn.vkapp.presentation.comments.CommentsFragment
import android.learn.vkapp.presentation.group.adapter.WallAdapter
import android.learn.vkapp.utils.getAccessToken
import android.view.View.GONE
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import android.learn.vkapp.presentation.group.adapter.LoadStateAdapter
import androidx.core.view.isVisible
import androidx.paging.LoadState
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.math.absoluteValue

class GroupFragment : Fragment() {
    private var groupId: String? = null

    private var _binding: FragmentGroupBinding? = null
    private val binding: FragmentGroupBinding
        get() = _binding ?: throw RuntimeException("FragmentGroupBinding is null")

    private lateinit var wallViewModel: WallViewModel
    private lateinit var adapter: WallAdapter

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
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        arguments?.let {
//            groupId = it.getString(ARG_PARAM1)
//        }

        requireArguments().getString(ID).let {
            groupId = it
        }

        initAdapter()

        binding.tryLoadButton.setOnClickListener {
            binding.errorText.visibility = GONE
            binding.tryLoadButton.visibility = GONE
            loadWall()
        }

        observeViewModel()

//        loadWall()

    }

    private fun observeViewModel() {
        wallViewModel = ViewModelProvider(this, viewModelFactory)[WallViewModel::class.java]
        loadWall()
//        wallViewModel.state.observe(viewLifecycleOwner) {
//            when (it) {
//                Error -> {
//                    binding.progressBar.visibility = GONE
//                    binding.errorText.visibility = VISIBLE
//                    binding.tryLoadButton.visibility = VISIBLE
//                }
//
//                Progress -> {
//                    binding.progressBar.visibility = VISIBLE
//                }
//
//                is Result -> {
//                    if (it.result != null) {
//                        adapter.submitList(
//                            ItemWallMapper().mapToItemWall(it.result)
//                        )
//                    }
//                    binding.progressBar.visibility = GONE
//                }
//            }
//        }
    }

    private fun loadWall() {
        val groupIdVal = groupId
        if (groupIdVal != null) {
            lifecycleScope.launch {
                wallViewModel.loadWall("-$groupIdVal").collect {
                    it.let {
                        adapter.submitData(lifecycle, it)
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        val onLikeClick = object : WallAdapter.OnLikeClickListener {
            override fun onLikeClick(itemWall: ItemWall, position: Int) {
                lifecycleScope.launch {
                    val response = wallViewModel.addLike(
                        getAccessToken(),
                        LIKE_OBJECT,
                        itemWall.id.toLong().absoluteValue,
                        -itemWall.ownerId.toLong()
                    )
                    response.response.count.let {
                        adapter.updateLikes(
                            adapter.snapshot().items,
                            it.toString(),
                            position
                        )
                    }
                }
            }
        }
        val onDislikeClick = object : WallAdapter.OnDislikeClickListener {
            override fun onDislikeClick(itemWall: ItemWall, position: Int) {
                lifecycleScope.launch {
                    val response = wallViewModel.deleteLike(
                        getAccessToken(),
                        LIKE_OBJECT,
                        itemWall.id.toLong().absoluteValue,
                        -itemWall.ownerId.toLong()
                    )
                    response.response.count.let {
                        adapter.updateLikes(
                            adapter.snapshot().items,
                            it.toString(),
                            position,
                            false
                        )
                    }
                }
            }
        }
        val onGotoCommentsClickListener = object : WallAdapter.OnGotoCommentsClickListener {
            //            override fun onGotoCommentsClick(postId: String, ownerId: String) {
//                parentFragmentManager.beginTransaction()
//                    .add(
//                        R.id.home_container,
//                        CommentsFragment.newInstance(postId = postId, ownerId = ownerId)
//                    ).hide(parentFragmentManager.fragments.last()).addToBackStack(null).commit()
//            }
            override fun onGotoCommentsClick(postId: String, ownerId: String) {
                val navHostFragment = parentFragmentManager.findFragmentById(R.id.home_container);

                val args = Bundle().apply {
                    putString(CommentsFragment.POST_ID, postId)
                    putString(CommentsFragment.OWNER_ID, ownerId)
                }
                if (navHostFragment != null) {
                    val navController = navHostFragment.findNavController()
                    navController.navigate(R.id.action_groupFragment_to_commentsFragment, args)
                }
            }
        }
        adapter = WallAdapter(
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick,
            onGotoCommentsClickListener = onGotoCommentsClickListener
        )
        binding.groupWallRv.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter(),
            footer = LoadStateAdapter()
        )
        adapter.addLoadStateListener { loadState ->
            binding.groupWallRv.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.tryLoadButton.isVisible = loadState.source.refresh is LoadState.Error
        }
    }

    companion object {
        const val ID = "id"
        private const val ARG_PARAM1 = "id"
        private const val LIKE_OBJECT = "post"

        fun newInstance(param1: String) =
            GroupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}