package com.dicoding.myapplication.githubappwithapi.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.myapplication.githubappwithapi.background.retrofit.ApiConfig
import com.dicoding.myapplication.githubappwithapi.background.response.GitUserResponse
import com.dicoding.myapplication.githubappwithapi.background.SettingPreferences
import com.dicoding.myapplication.githubappwithapi.background.Users
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val pref: SettingPreferences) : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<Users>>()

    fun setGithubUsers(query: String) {
        ApiConfig.getGithubApi()
            .getGithubUsers(query)
            .enqueue(object : Callback<GitUserResponse> {
                override fun onResponse(
                    call: Call<GitUserResponse>,
                    response: Response<GitUserResponse>
                ) {
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body()?.items)
                    }

                }

                override fun onFailure(call: Call<GitUserResponse>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.message}")
                }

            })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getGithubUsers(): LiveData<ArrayList<Users>> {
        return listUsers
    }
}