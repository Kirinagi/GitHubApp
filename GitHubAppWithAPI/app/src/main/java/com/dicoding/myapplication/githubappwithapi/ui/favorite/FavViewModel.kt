package com.dicoding.myapplication.githubappwithapi.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.myapplication.githubappwithapi.background.database.FavUser
import com.dicoding.myapplication.githubappwithapi.background.database.FavUserDao
import com.dicoding.myapplication.githubappwithapi.background.database.GitUserDatabase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavViewModel(application: Application): AndroidViewModel(application) {
    private var userDao: FavUserDao?
    private var userDb: GitUserDatabase? = GitUserDatabase.getDatabase(application)

    init {
        userDao = userDb?.favUserDao()
    }

    fun getFavGitUser(): LiveData<List<FavUser>>?{
        return userDao?.getFavUser()
    }
}