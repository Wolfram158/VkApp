package android.learn.vkapp.presentation.groups

import android.content.Context
import android.learn.vkapp.R
import android.learn.vkapp.data.mapper.GroupsMapper
import android.learn.vkapp.data.network.dto.GroupsResponseDto
import android.learn.vkapp.databinding.FragmentFoundGroupsBinding
import android.learn.vkapp.presentation.App
import android.learn.vkapp.presentation.ViewModelFactory
import android.learn.vkapp.presentation.group.GroupFragment
import android.learn.vkapp.presentation.groups.adapter.GroupsAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import javax.inject.Inject

class FoundGroupsFragment : Fragment() {
    private var _binding: FragmentFoundGroupsBinding? = null
    private val binding: FragmentFoundGroupsBinding
        get() = _binding ?: throw RuntimeException("FragmentFoundGroupsBinding is null")

    private var viewModel: FoundGroupsViewModel? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as App).component
    }

    private val adapter by lazy {
        GroupsAdapter()
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
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

        if (savedInstanceState == null) {
            Log.d("Debug", "new")
            observeViewModel()
        }

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
                    .replace(R.id.home_container, GroupFragment.newInstance(id))
                    .addToBackStack(null).commit()
            }
        }
    }

    private fun observeViewModel() {
        val fragment = parentFragmentManager.findFragmentByTag(HostGroupsFragment.TAG)
        viewModel = fragment?.let { ViewModelProvider(it, viewModelFactory) }
            ?.get(FoundGroupsViewModel::class.java)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                            Log.d("Debug", "FoundGroupsFragment $FoundGroupsFragment")
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
        }
    }

    override fun onPause() {
        Log.d("Debug", "Pause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("Debug", "Stop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("Debug", "Destroy")
        super.onDestroy()
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