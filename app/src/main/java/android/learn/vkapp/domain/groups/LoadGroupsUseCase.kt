package android.learn.vkapp.domain.groups

import javax.inject.Inject

class LoadGroupsUseCase @Inject constructor(
    private val repository: GroupsRepository
) {
    suspend operator fun invoke(token: String) = repository.loadGroups(token)
}