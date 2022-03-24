package com.dicoding.myapplication.githubappwithapi.background.response

data class DetailGitUserResponse(
    val login: String,
    val avatar_url: String,
    val followers_url: String,
    val following_url: String,
    val name: String,
    val company: String,
    val location: String,
    val followers: Int,
    val following: Int,
    val public_repos: Int
)
