package com.dicoding.myapplication.githubappwithapi.background.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")
data class FavUser(
    val login: String,

    @PrimaryKey
    val id: Int,
    val avatar_url : String
)
