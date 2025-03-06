package android.learn.vkapp.domain.group

import javax.inject.Inject

class LoadWallUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    operator fun invoke(id: String) =
        repository.loadWall(id)
}