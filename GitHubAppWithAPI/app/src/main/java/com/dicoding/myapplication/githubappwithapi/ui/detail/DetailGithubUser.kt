package com.dicoding.myapplication.githubappwithapi.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.myapplication.githubappwithapi.R
import com.dicoding.myapplication.githubappwithapi.databinding.ActivityDetailGithubUserBinding
import com.dicoding.myapplication.githubappwithapi.ui.follow.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*

@InternalCoroutinesApi
class DetailGithubUser : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
    }

    private lateinit var detailGithubUserBinding: ActivityDetailGithubUserBinding
    private lateinit var viewModel: DetailGithubUserViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        detailGithubUserBinding = ActivityDetailGithubUserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(detailGithubUserBinding.root)

        supportActionBar?.hide()

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        showProgressBar(true)

        viewModel = ViewModelProvider(this)[DetailGithubUserViewModel::class.java]
        viewModel .setGithubUsersDetail(username!!)
        viewModel.getGithubUsersDetail().observe(this) {
            if(it != null){
                detailGithubUserBinding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvCompany.text = it.company
                    followers.text = "${it.followers} Followers"
                    following.text = "${it.following} Following"
                    tvLocation.text = it.location
                    tvRepository.text = "${it.public_repos} Repository"
                    showProgressBar(false)
                    Glide.with(this@DetailGithubUser)
                        .load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .circleCrop()
                        .into(ivDetailavatar)
                }
            }
        }

        var isCheck = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null){
                    if (count > 0 ){
                        detailGithubUserBinding.toggleFav.isChecked = true
                        isCheck = true
                    }else{
                        detailGithubUserBinding.toggleFav.isChecked = false
                        isCheck = false
                    }
                }
            }
        }

        detailGithubUserBinding.toggleFav.setOnClickListener {
            isCheck = !isCheck
            if (isCheck){
                viewModel.addUserToFav(username,id,avatarUrl!!)
            }else{
                viewModel.removeFromFav(id)
            }
            detailGithubUserBinding.toggleFav.isChecked = isCheck
        }


        val pagerAdapter = PagerAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.vp_android)
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = findViewById(R.id.tl_github)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun showProgressBar(state: Boolean) {
        if (state) {
            detailGithubUserBinding.progressBar.visibility = View.VISIBLE
        } else {
            detailGithubUserBinding.progressBar.visibility = View.INVISIBLE
        }
    }
}