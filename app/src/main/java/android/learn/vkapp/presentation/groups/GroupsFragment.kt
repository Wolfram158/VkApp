package android.learn.vkapp.presentation.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.learn.vkapp.R
import android.learn.vkapp.data.mapper.GroupsMapper
import android.learn.vkapp.databinding.FragmentGroupsBinding
import android.learn.vkapp.presentation.group.GroupFragment
import android.learn.vkapp.presentation.groups.adapter.GroupsAdapter
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import com.vk.id.VKID
import java.lang.RuntimeException

class GroupsFragment : Fragment() {
    private var _binding: FragmentGroupsBinding? = null
    private val binding: FragmentGroupsBinding
        get() = _binding ?: throw RuntimeException("FragmentGroupsBinding is null")

    private lateinit var groupsViewModel: GroupsViewModel
    private lateinit var adapter: GroupsAdapter

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
        VKID.instance.accessToken?.token?.let {
            groupsViewModel.loadGroups(it)
        }
    }

    private fun observeViewModel() {
        groupsViewModel = ViewModelProvider(this)[GroupsViewModel::class.java]
        groupsViewModel.state.observe(viewLifecycleOwner) {
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
                    adapter.submitList(
                        GroupsMapper().mapToGroups(
                            it.result
                        ).groups
                    )
                    binding.progressBar.visibility = GONE
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
        fun newInstance() = GroupsFragment()
    }
}