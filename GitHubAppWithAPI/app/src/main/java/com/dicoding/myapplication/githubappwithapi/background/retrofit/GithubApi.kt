package com.dicoding.myapplication.githubappwithapi.background.retrofit

import com.dicoding.myapplication.githubappwithapi.background.response.DetailGitUserResponse
import com.dicoding.myapplication.githubappwithapi.background.response.GitUserResponse
import com.dicoding.myapplication.githubappwithapi.background.Users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
        @GET("search/users")
        @Headers("Authorization: token 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")
        fun getGithubUsers(@Query("q") query: String): Call<GitUserResponse>


        @GET("users/{username}")
        @Headers("Authorization: token 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")
        fun getGithubFullname(
            @Path("username") username: String
        ): Call<DetailGitUserResponse>

        @GET("users/{username}/following")
        @Headers("Authorization: token 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")
        fun getGithubFollowerList(
            @Path("username") username: String
        ):Call<ArrayList<Users>>

        @GET("users/{username}/followers")
        @Headers("Authorization: token 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")
        fun getGithubFollowingList(
            @Path("username") username: String
        ):Call<ArrayList<Users>>



}