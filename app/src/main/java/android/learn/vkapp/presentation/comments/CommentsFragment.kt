package android.learn.vkapp.presentation.comments

import android.content.Context
import android.learn.vkapp.data.mapper.CommentsMapper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.learn.vkapp.databinding.FragmentCommentsBinding
import android.learn.vkapp.domain.comments.Comment
import android.learn.vkapp.presentation.App
import android.learn.vkapp.presentation.ViewModelFactory
import android.learn.vkapp.presentation.comments.adapter.CommentsAdapter
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.vk.id.VKID
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.math.absoluteValue

class CommentsFragment : Fragment() {
    private var _binding: FragmentCommentsBinding? = null
    private val binding: FragmentCommentsBinding
        get() = _binding ?: throw RuntimeException("FragmentCommentsBinding is null")

    private var postId: String? = null
    private var ownerId: String? = null

    private lateinit var commentsViewModel: CommentsViewModel
    private lateinit var adapter: CommentsAdapter

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
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            postId = it.getString(POST_ID)
            ownerId = it.getString(OWNER_ID)
        }

        initAdapter()

        observeViewModel()

        binding.tryLoadButton.setOnClickListener {
            binding.errorText.visibility = GONE
            binding.tryLoadButton.visibility = GONE
            loadComments()
        }

        loadComments()

    }

    private fun loadComments() {
        val postIdVal = postId
        val ownerIdVal = "-$ownerId"
        if (postIdVal != null) {
            VKID.instance.accessToken?.token?.let {
                commentsViewModel.loadComments(token = it, postId = postIdVal, ownerId = ownerIdVal)
            }
        }
    }

    private fun observeViewModel() {
        commentsViewModel = ViewModelProvider(this, viewModelFactory)[CommentsViewModel::class.java]
        commentsViewModel.state.observe(viewLifecycleOwner) {
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
                            CommentsMapper().mapToComments(it.result)
                        )
                    }
                    binding.progressBar.visibility = GONE
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = CommentsAdapter()
        binding.commentsRv.adapter = adapter
        adapter.onLikeClick = object : CommentsAdapter.OnLikeClickListener {
            override fun onLikeClick(comment: Comment, position: Int) {
                lifecycleScope.launch {
                    val response = VKID.instance.accessToken?.token?.let {
                        commentsViewModel.addLike(
                            it, "comment", comment.id.toLong().absoluteValue,
                            comment.ownerId.toLong()
                        )
                    }
                    response?.response?.count?.let { adapter.updateLikes(it.toString(), position) }
                }
            }
        }
        adapter.onDislikeClick = object : CommentsAdapter.OnDislikeClickListener {
            override fun onDislikeClick(comment: Comment, position: Int) {
                lifecycleScope.launch {
                    val response = VKID.instance.accessToken?.token?.let {
                        commentsViewModel.deleteLike(
                            it, "comment", comment.id.toLong().absoluteValue,
                            comment.ownerId.toLong()
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
    }

    companion object {
        private const val POST_ID = "post_id"
        private const val OWNER_ID = "owner_id"

        fun newInstance(postId: String, ownerId: String) =
            CommentsFragment().apply {
                arguments = Bundle().apply {
                    putString(POST_ID, postId)
                    putString(OWNER_ID, ownerId)
                }
            }
    }
}