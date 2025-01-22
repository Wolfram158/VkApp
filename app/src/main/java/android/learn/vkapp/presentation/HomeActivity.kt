package android.learn.vkapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.learn.vkapp.R
import android.learn.vkapp.databinding.ActivityHomeBinding
import android.learn.vkapp.presentation.groups.GroupsFragment
import android.learn.vkapp.presentation.news.NewsFragment

class HomeActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_groups -> {
//                    val fragment = supportFragmentManager.findFragmentByTag(GroupsFragment.TAG)
//                    if (fragment != null) {
//                        supportFragmentManager.beginTransaction().remove(fragment).commit()
//                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_container, GroupsFragment.newInstance())
                        //.addToBackStack(null)
                        .commit()
                }

                R.id.item_news -> {
//                    val fragment = supportFragmentManager.findFragmentByTag(NewsFragment.TAG)
//                    if (fragment != null) {
//                        supportFragmentManager.beginTransaction().remove(fragment).commit()
//                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_container, NewsFragment.newInstance())
                        //.addToBackStack(null)
                        .commit()
                }

                R.id.item_friends -> {

                }
            }
            true
        }

        binding.bottomNavigation.selectedItemId = R.id.item_news
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)

    }
}