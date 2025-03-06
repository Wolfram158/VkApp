package android.learn.vkapp.data.network

import android.learn.vkapp.data.mapper.ItemWallMapper
import android.learn.vkapp.domain.group.ItemWall
import android.learn.vkapp.utils.getAccessToken
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay

class WallPageSource(
    private val ownerId: String,
    private val apiService: ApiService
) : PagingSource<Int, ItemWall>() {
    override fun getRefreshKey(state: PagingState<Int, ItemWall>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemWall> {
        return try {
            val offset = params.key ?: 0
            val response = apiService.loadWall(
                token = getAccessToken(),
                id = ownerId,
                extended = "1",
                startFrom = offset.toString(),
                count = params.loadSize.toString()
            )
            delay(10000)
            LoadResult.Page(
                data = ItemWallMapper().mapToItemWall(response),
                prevKey = if (offset == 0) null else offset - 10,
                nextKey = if (response.response?.items?.isEmpty() == true) null else offset + 10
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}