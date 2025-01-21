package android.learn.vkapp.data.repository

import android.learn.vkapp.data.network.ApiService
import android.learn.vkapp.data.network.dto.GroupsResponseDto
import android.learn.vkapp.domain.groups.GroupsRepository
import javax.inject.Inject

class GroupsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : GroupsRepository {
    override suspend fun loadGroups(token: String): GroupsResponseDto {
        return apiService.loadGroups(token, "1", "members_count")
    }
}