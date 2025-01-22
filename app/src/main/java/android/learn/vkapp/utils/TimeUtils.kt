package android.learn.vkapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun fromUnixToReadable(unixTime: Long): String {
    val d = Date(unixTime * 1000L)
    return SimpleDateFormat("yyyy dd MMM HH:mm", Locale.getDefault()).format(d)
}