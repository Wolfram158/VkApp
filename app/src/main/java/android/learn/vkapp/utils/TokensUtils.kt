package android.learn.vkapp.utils

import com.vk.id.VKID

fun getAccessToken(): String {
    return VKID.instance.accessToken?.token ?: throw RuntimeException("Access token is null")
}