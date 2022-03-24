package com.dicoding.myapplication.githubappwithapi.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.myapplication.githubappwithapi.background.database.FavUser
import com.dicoding.myapplication.githubappwithapi.background.database.FavUserDao
import com.dicoding.myapplication.githubappwithapi.background.database.GitUserDatabase
import com.dicoding.myapplication.githubappwithapi.background.response.DetailGitUserResponse
import com.dicoding.myapplication.githubappwithapi.background.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@InternalCoroutinesApi
class DetailGithubUserViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao: FavUserDao?
    private var userDb: GitUserDatabase? = GitUserDatabase.getDatabase(application)

    init {
        userDao = userDb?.favUserDao()
    }

    val listUsersDetail = MutableLiveData<DetailGitUserResponse>()

    fun setGithubUsersDetail(username: String) {
        ApiConfig.getGithubApi()
            .getGithubFullname(username)
            .enqueue(object : Callback<DetailGitUserResponse> {
                override fun onResponse(
                    call: Call<DetailGitUserResponse>,
                    response: Response<DetailGitUserResponse>
                ) {
                    if (response.isSuccessful) {
                        listUsersDetail.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailGitUserResponse>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.message}")
                }

            })
    }

    fun getGithubUsersDetail(): LiveData<DetailGitUserResponse> {
        return listUsersDetail
    }

    fun addUserToFav(username: String, id:Int, avatarUrl: String){
        CoroutineScope(Dispatchers.IO).launch {
            val users = FavUser(
                username,
                id,
                avatarUrl
            )
            userDao?.addToFav(users)
        }
    }
    suspend fun checkUser(id:Int) = userDao?.checkUser(id)

    fun removeFromFav(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFav(id)
        }
    }
}