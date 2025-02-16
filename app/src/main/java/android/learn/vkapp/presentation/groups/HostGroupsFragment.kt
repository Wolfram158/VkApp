package android.learn.vkapp.presentation.groups

import android.content.Context
import android.learn.vkapp.R
import android.learn.vkapp.databinding.FragmentHostGroupsBinding
import android.learn.vkapp.presentation.App
import android.learn.vkapp.presentation.ViewModelFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class HostGroupsFragment : Fragment() {
    private var _binding: FragmentHostGroupsBinding? = null
    private val binding: FragmentHostGroupsBinding
        get() = _binding ?: throw RuntimeException("FragmentHostGroupsBinding is null")

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
        _binding = FragmentHostGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            parentFragmentManager.beginTransaction().add(
                R.id.groups_host_container,
                FoundGroupsFragment.newInstance(),
                FoundGroupsFragment.TAG
            ).add(
                R.id.groups_host_container,
                UserGroupsFragment.newInstance(),
                UserGroupsFragment.TAG
            ).commit()

            val fragment = parentFragmentManager.findFragmentByTag(FoundGroupsFragment.TAG)
            if (fragment != null) {
                parentFragmentManager.beginTransaction().hide(fragment).commit()
            }

            val viewModel = ViewModelProvider(this, viewModelFactory)[FoundGroupsViewModel::class.java]

            binding.findGroupField.addTextChangedListener { s ->
                if (s?.length == 0) {
                    val fragment1 = parentFragmentManager.findFragmentByTag(FoundGroupsFragment.TAG)
                    val fragment2 = parentFragmentManager.findFragmentByTag(UserGroupsFragment.TAG)
                    if (fragment1 != null && fragment2 != null) {
                        parentFragmentManager.beginTransaction().show(fragment2).hide(fragment1)
                            .commit()
                    }
                } else {
                    val fragment1 = parentFragmentManager.findFragmentByTag(FoundGroupsFragment.TAG)
                    val fragment2 = parentFragmentManager.findFragmentByTag(UserGroupsFragment.TAG)
                    fragment2?.isVisible?.let {
                        if (it && fragment1 != null) {
                            parentFragmentManager.beginTransaction().hide(fragment2).show(fragment1)
                                .commit()
                        }
                    }
                    Log.d("Debug", "before query")
                    viewModel.changeQuery(s.toString())
                }
            }
        }
    }

    companion object {
        const val TAG = "HostGroupsFragment"

        fun newInstance() =
            HostGroupsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}