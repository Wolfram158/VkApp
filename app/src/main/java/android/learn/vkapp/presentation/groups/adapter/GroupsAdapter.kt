package android.learn.vkapp.presentation.groups.adapter

import android.learn.vkapp.databinding.ItemGroupBinding
import android.learn.vkapp.domain.groups.Group
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso

class GroupsAdapter : ListAdapter<Group, GroupsViewHolder>(GroupsDiffUtilCallback) {
    var onGroupClickListener: OnGroupClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        val binding = ItemGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GroupsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            groupMembersCount.text = item.membersCount
            groupName.text = item.name
            Picasso.get().load(item.photo).into(groupImage)
            root.setOnClickListener {
                onGroupClickListener?.onGroupClick(item.id)
            }
        }

    }

    interface OnGroupClickListener {
        fun onGroupClick(id: String)
    }

}