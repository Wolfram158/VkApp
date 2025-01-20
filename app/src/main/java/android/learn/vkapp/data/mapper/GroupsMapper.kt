package android.learn.vkapp.data.mapper

import android.learn.vkapp.data.network.dto.GroupsResponseDto
import android.learn.vkapp.domain.groups.Group
import android.learn.vkapp.domain.groups.Groups

class GroupsMapper {
    fun mapToGroups(groupsResponse: GroupsResponseDto): Groups {
        return Groups(
            count = groupsResponse.groups.count,
            groups = groupsResponse.groups.items.map { groupDto ->
                Group(
                    id = groupDto.id,
                    name = groupDto.name,
                    isClosed = groupDto.isClosed,
                    photo = groupDto.photo,
                    screenName = groupDto.screenName,
                    membersCount = groupDto.membersCount
                )
            })
    }
}