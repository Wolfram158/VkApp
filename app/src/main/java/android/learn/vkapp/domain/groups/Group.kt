package android.learn.vkapp.domain.groups

data class Group(
    val id: String,
    val name: String,
    val isClosed: String,
    val photo: String,
    val screenName: String,
    val membersCount: String
)