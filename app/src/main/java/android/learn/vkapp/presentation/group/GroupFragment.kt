package android.learn.vkapp.presentation.group

import android.learn.vkapp.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.learn.vkapp.data.mapper.ItemWallMapper
import android.learn.vkapp.data.network.ApiFactory
import android.learn.vkapp.databinding.FragmentGroupBinding
import android.learn.vkapp.domain.group.ItemWall
import android.learn.vkapp.presentation.comments.CommentsFragment
import android.learn.vkapp.presentation.group.adapter.WallAdapter
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.vk.id.VKID
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import kotlin.math.absoluteValue

class GroupFragment : Fragment() {
    private var groupId: String? = null

    private var _binding: FragmentGroupBinding? = null
    private val binding: FragmentGroupBinding
        get() = _binding ?: throw RuntimeException("FragmentGroupBinding is null")

    private lateinit var wallViewModel: WallViewModel
    private lateinit var adapter: WallAdapter

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
        wallViewModel = ViewModelProvider(this)[WallViewModel::class.java]
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
            VKID.instance.accessToken?.token?.let {
                wallViewModel.loadWall(it, groupIdVal)
            }
        }
    }

    private fun initAdapter() {
        adapter = WallAdapter()
        binding.groupWallRv.adapter = adapter
        adapter.onLikeClick = object : WallAdapter.OnLikeClickListener {
            override fun onLikeClick(itemWall: ItemWall, position: Int) {
                lifecycleScope.launch {
                    val response = VKID.instance.accessToken?.token?.let {
                        ApiFactory.apiService.addLike(
                            it,
                            "post",
                            itemWall.id.toLong().absoluteValue,
                            -itemWall.ownerId.toLong()
                        )
                    }
                    response?.response?.count?.let { adapter.updateLikes(it.toString(), position) }
                }
            }
        }
        adapter.onDislikeClick = object : WallAdapter.OnDislikeClickListener {
            override fun onDislikeClick(itemWall: ItemWall, position: Int) {
                lifecycleScope.launch {
                    val response = VKID.instance.accessToken?.token?.let {
                        ApiFactory.apiService.deleteLike(
                            it,
                            "post",
                            itemWall.id.toLong().absoluteValue,
                            -itemWall.ownerId.toLong()
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
        adapter.onGotoCommentsClickListener = object : WallAdapter.OnGotoCommentsClickListener {
            override fun onGotoCommentsClick(postId: String, ownerId: String) {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.home_container,
                        CommentsFragment.newInstance(postId = postId, ownerId = ownerId)
                    )
                    .addToBackStack(null).commit()
            }
        }
    }

    companion object {
        private const val ARG_PARAM1 = "id"

        fun newInstance(param1: String) =
            GroupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}