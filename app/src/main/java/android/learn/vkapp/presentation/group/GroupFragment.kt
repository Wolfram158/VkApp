package android.learn.vkapp.presentation.group

import android.content.Context
import android.learn.vkapp.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.learn.vkapp.data.mapper.ItemWallMapper
import android.learn.vkapp.databinding.FragmentGroupBinding
import android.learn.vkapp.domain.group.ItemWall
import android.learn.vkapp.presentation.App
import android.learn.vkapp.presentation.ViewModelFactory
import android.learn.vkapp.presentation.comments.CommentsFragment
import android.learn.vkapp.presentation.group.adapter.WallAdapter
import android.learn.vkapp.utils.getAccessToken
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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

        arguments?.let {
            groupId = it.getString(ARG_PARAM1)
        }

        initAdapter()

        binding.tryLoadButton.setOnClickListener {
            binding.errorText.visibility = GONE
            binding.tryLoadButton.visibility = GONE
            loadWall()
        }

        observeViewModel()

        loadWall()

    }

    private fun observeViewModel() {
        wallViewModel = ViewModelProvider(this, viewModelFactory)[WallViewModel::class.java]
        wallViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                Error -> {
                    binding.progressBar.visibility = GONE
                    binding.errorText.visibility = VISIBLE
                    binding.tryLoadButton.visibility = VISIBLE
                }

                Progress -> {
                    binding.progressBar.visibility = VISIBLE
                }

                is Result -> {
                    if (it.result != null) {
                        adapter.submitList(
                            ItemWallMapper().mapToItemWall(it.result)
                        )
                    }
                    binding.progressBar.visibility = GONE
                }
            }
        }
    }

    private fun loadWall() {
        val groupIdVal = groupId
        if (groupIdVal != null) {
            wallViewModel.loadWall(getAccessToken(), groupIdVal)
        }
    }

    private fun initAdapter() {
        adapter = WallAdapter()
        binding.groupWallRv.adapter = adapter
        adapter.onLikeClick = object : WallAdapter.OnLikeClickListener {
            override fun onLikeClick(itemWall: ItemWall, position: Int) {
                lifecycleScope.launch {
                    val response = wallViewModel.addLike(
                        getAccessToken(),
                        LIKE_OBJECT,
                        itemWall.id.toLong().absoluteValue,
                        -itemWall.ownerId.toLong()
                    )
                    response.response.count.let { adapter.updateLikes(it.toString(), position) }
                }
            }
        }
        adapter.onDislikeClick = object : WallAdapter.OnDislikeClickListener {
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
                            it.toString(),
                            position,
                            false
                        )
                    }
                }
            }
        }
        adapter.onGotoCommentsClickListener = object : WallAdapter.OnGotoCommentsClickListener {
            override fun onGotoCommentsClick(postId: String, ownerId: String) {
                parentFragmentManager.beginTransaction()
                    .add(
                        R.id.home_container,
                        CommentsFragment.newInstance(postId = postId, ownerId = ownerId)
                    ).hide(parentFragmentManager.fragments.last()).addToBackStack(null).commit()
            }
        }
    }

    companion object {
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