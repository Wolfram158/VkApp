package android.learn.vkapp.presentation.groups

import android.learn.vkapp.R
import android.learn.vkapp.data.mapper.GroupsMapper
import android.learn.vkapp.data.network.dto.GroupsResponseDto
import android.learn.vkapp.databinding.FragmentFoundGroupsBinding
import android.learn.vkapp.presentation.group.GroupFragment
import android.learn.vkapp.presentation.groups.adapter.GroupsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class FoundGroupsFragment : Fragment() {
    private var _binding: FragmentFoundGroupsBinding? = null
    private val binding: FragmentFoundGroupsBinding
        get() = _binding ?: throw RuntimeException("FragmentFoundGroupsBinding is null")

    private var viewModel: FoundGroupsViewModel? = null

    private val adapter by lazy {
        GroupsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoundGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        observeViewModel()

        binding.tryLoadButton.setOnClickListener {
            binding.errorText.visibility = GONE
            binding.tryLoadButton.visibility = GONE
            lifecycleScope.launch {
                when (val value = viewModel?.groups?.value) {
                    is Error<*> -> {
                        adapter.submitList(listOf())
                        if (value.query is String) {
                            viewModel?.loadGroups(value.query)
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun initAdapter() {
        binding.foundGroupsRv.adapter = adapter
        adapter.onGroupClickListener = object : GroupsAdapter.OnGroupClickListener {
            override fun onGroupClick(id: String) {
                parentFragmentManager.beginTransaction()
                    .add(R.id.home_container, GroupFragment.newInstance(id))
                    .addToBackStack(null).commit()
            }
        }
    }

    private fun observeViewModel() {
        val fragment = parentFragmentManager.findFragmentByTag(HostGroupsFragment.TAG)
        viewModel = fragment?.let { ViewModelProvider(it) }?.get(FoundGroupsViewModel::class.java)
        viewModel?.observe(500, lifecycleScope) {
            when (it) {
                is Error<*> -> {
                    adapter.submitList(listOf())
                    binding.progressBar.visibility = GONE
                    binding.errorText.visibility = VISIBLE
                    binding.tryLoadButton.visibility = VISIBLE
                }

                Initial -> {}
                is Progress<*> -> {
                    binding.progressBar.visibility = VISIBLE
                    binding.errorText.visibility = GONE
                    binding.tryLoadButton.visibility = GONE
                    viewModel?.loadGroups(it.progress.toString())
                }

                is Result<*> -> {
                    binding.progressBar.visibility = GONE
                    if (it.result is GroupsResponseDto) {
                        adapter.submitList(GroupsMapper().mapToGroups(it.result).groups)
                    }
                }

            }
        }
    }

    companion object {
        const val TAG = "FoundGroupsFragment"

        fun newInstance() =
            FoundGroupsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}