package android.learn.vkapp.presentation

import android.learn.vkapp.databinding.ActivityMainBinding
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.vk.id.VKID
import com.vk.id.auth.VKIDAuthUiParams

class LoginActivity : ComponentActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        try {
            VKID.init(this)
            VKID.logsEnabled = true

        } catch (_: Exception) {
        }

        binding.oneTap.authParams = VKIDAuthUiParams.Builder().apply {
            scopes = setOf("wall", "friends", "messages")
        }.build()

        binding.oneTap.setCallbacks(
            onAuth = { _, token ->
                Log.d("onAuthVK", token.token)
                startActivity(HomeActivity.newIntent(this))
            },
            onFail = { _, fail ->
                Log.d("onFailVK", fail.description)
            },
            onAuthCode = { data, _ ->
                Log.d("onAuthCodeVK", data.code)
            }
        )

    }
}