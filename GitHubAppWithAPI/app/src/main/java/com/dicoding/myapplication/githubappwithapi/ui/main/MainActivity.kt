package com.dicoding.myapplication.githubappwithapi.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myapplication.githubappwithapi.R
import com.dicoding.myapplication.githubappwithapi.background.SettingPreferences
import com.dicoding.myapplication.githubappwithapi.background.Users
import com.dicoding.myapplication.githubappwithapi.background.ViewModelFactory
import com.dicoding.myapplication.githubappwithapi.databinding.ActivityMainBinding
import com.dicoding.myapplication.githubappwithapi.ui.detail.DetailGithubUser
import com.dicoding.myapplication.githubappwithapi.ui.favorite.FavActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.InternalCoroutinesApi

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: UserAdapter


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        supportActionBar?.title = "GitHub"

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                Intent(this@MainActivity, DetailGithubUser::class.java).also {
                    it.putExtra(DetailGithubUser.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailGithubUser.EXTRA_ID, data.id)
                    it.putExtra(DetailGithubUser.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        val pref = SettingPreferences.getInstance(dataStore)

        viewModel = ViewModelProvider(
            this,ViewModelFactory(pref)
    //            ViewModelProvider.NewInstanceFactory()
        )[UserViewModel::class.java]


        viewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }

        activityMainBinding.apply {
            showRecyclerList()
            btnSearch.setOnClickListener {
                searchGitUser()
            }
            edtSearch.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchGitUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
//            fabAdd.setOnClickListener {
//                val intent = Intent(this@MainActivity, DarkMode::class.java)
//                startActivity(intent)
//            }
        }
        viewModel.getGithubUsers().observe(this) {
            if (it != null) {
                adapter.setList(it)
                showProgressBar(false)
            }
        }
    }

    private fun showProgressBar(state: Boolean) {
        if (state) {
            activityMainBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityMainBinding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activityMainBinding.rvSearchUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            activityMainBinding.rvSearchUser.layoutManager = LinearLayoutManager(this)
        }
        activityMainBinding.rvSearchUser.adapter = adapter
    }

    private fun searchGitUser() {
        activityMainBinding.apply {
            val query = edtSearch.text.toString()
            if (query.isEmpty())
                return
            showProgressBar(true)
            viewModel.setGithubUsers(query)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.fav_menu ->{
                Intent(this, FavActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}