package android.learn.vkapp.domain.group

import javax.inject.Inject

class LoadWallUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(token: String, id: String, extended: String) =
        repository.loadWall(token, id, extended)
}