package com.dicoding.myapplication.githubappwithapi.ui.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myapplication.githubappwithapi.background.retrofit.ApiConfig
import com.dicoding.myapplication.githubappwithapi.background.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelFollower: ViewModel() {
    val followerList = MutableLiveData<ArrayList<Users>>()

    fun setFollowerList(username: String){
        ApiConfig.getGithubApi()
            .getGithubFollowerList(username)
            .enqueue(object : Callback<ArrayList<Users>>{
                override fun onResponse(
                    call: Call<ArrayList<Users>>,
                    response: Response<ArrayList<Users>>
                ) {
                    if (response.isSuccessful){
                        followerList.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Users>>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.message}")
                }

            })
    }
    fun getFollowerList(): LiveData<ArrayList<Users>>{
        return followerList
    }
}