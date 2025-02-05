package android.learn.vkapp.presentation.groups

import android.content.Context
import android.learn.vkapp.R
import android.learn.vkapp.data.mapper.GroupsMapper
import android.learn.vkapp.data.network.dto.GroupsResponseDto
import android.learn.vkapp.databinding.FragmentGroupsBinding
import android.learn.vkapp.presentation.App
import android.learn.vkapp.presentation.ViewModelFactory
import android.learn.vkapp.presentation.group.GroupFragment
import android.learn.vkapp.presentation.groups.adapter.GroupsAdapter
import android.learn.vkapp.utils.getAccessToken
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class UserGroupsFragment : Fragment() {
    private var _binding: FragmentGroupsBinding? = null
    private val binding: FragmentGroupsBinding
        get() = _binding ?: throw RuntimeException("FragmentGroupsBinding is null")

    private lateinit var groupsViewModel: UserGroupsViewModel
    private lateinit var adapter: GroupsAdapter

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
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        binding.tryLoadButton.setOnClickListener {
            binding.errorText.visibility = GONE
            binding.tryLoadButton.visibility = GONE
            loadGroups()
        }

        observeViewModel()

        loadGroups()

    }

    private fun loadGroups() {
        groupsViewModel.loadGroups(getAccessToken())
    }

    private fun observeViewModel() {
        groupsViewModel = ViewModelProvider(this, viewModelFactory)[UserGroupsViewModel::class.java]
        groupsViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is Error<*> -> {
                    binding.progressBar.visibility = GONE
                    binding.errorText.visibility = VISIBLE
                    binding.tryLoadButton.visibility = VISIBLE
                }

                is Progress<*> -> {
                    binding.progressBar.visibility = VISIBLE
                }

                is Result<*> -> {
                    adapter.submitList(
                        GroupsMapper().mapToGroups(
                            it.result as GroupsResponseDto
                        ).groups
                    )
                    binding.progressBar.visibility = GONE
                }

                Initial -> {
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = GroupsAdapter()
        binding.groupsRv.adapter = adapter
        adapter.onGroupClickListener = object : GroupsAdapter.OnGroupClickListener {
            override fun onGroupClick(id: String) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.home_container, GroupFragment.newInstance(id))
                    .addToBackStack(null).commit()
            }
        }
    }

    companion object {
        const val TAG = "GroupsFragment"

        fun newInstance() = UserGroupsFragment()
    }
}