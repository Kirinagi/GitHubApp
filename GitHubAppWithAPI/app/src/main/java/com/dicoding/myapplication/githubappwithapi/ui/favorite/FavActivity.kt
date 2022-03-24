package com.dicoding.myapplication.githubappwithapi.ui.favorite

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myapplication.githubappwithapi.background.database.FavUser
import com.dicoding.myapplication.githubappwithapi.background.Users
import com.dicoding.myapplication.githubappwithapi.databinding.ActivityFavBinding
import com.dicoding.myapplication.githubappwithapi.ui.detail.DetailGithubUser
import com.dicoding.myapplication.githubappwithapi.ui.main.UserAdapter
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavActivity : AppCompatActivity() {

    private lateinit var activityFavBinding: ActivityFavBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFavBinding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(activityFavBinding.root)

        supportActionBar?.title = "Favorite User"

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this)[FavViewModel::class.java]
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                Intent(this@FavActivity, DetailGithubUser::class.java).also {
                    it.putExtra(DetailGithubUser.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailGithubUser.EXTRA_ID, data.id)
                    it.putExtra(DetailGithubUser.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        activityFavBinding.apply {
            rvFavuser.setHasFixedSize(true)
            showRecyclerList()
            rvFavuser.adapter = adapter
        }
        viewModel.getFavGitUser()?.observe(this) {
            if (it!=null){
                val list = mapList(it)
                adapter.setList(list)
            }
        }

    }

    private fun mapList(users: List<FavUser>): ArrayList<Users>{
        val listUsers = ArrayList<Users>()
        for (user in users) {
            val userMapped = Users(
                user.login,
                user.avatar_url,
                user.id

            )
            listUsers.add(userMapped)
        }
        return listUsers
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activityFavBinding.rvFavuser.layoutManager = GridLayoutManager(this, 2)
        } else {
            activityFavBinding.rvFavuser.layoutManager = LinearLayoutManager(this)
        }
        activityFavBinding.rvFavuser.adapter = adapter
    }
}