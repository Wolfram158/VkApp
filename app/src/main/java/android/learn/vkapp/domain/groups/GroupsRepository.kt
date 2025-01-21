package android.learn.vkapp.domain.groups

import android.learn.vkapp.data.network.dto.GroupsResponseDto

interface GroupsRepository {
    suspend fun loadGroups(token: String): GroupsResponseDto
}