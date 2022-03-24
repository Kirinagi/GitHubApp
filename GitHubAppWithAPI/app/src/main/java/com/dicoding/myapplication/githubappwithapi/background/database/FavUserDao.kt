package com.dicoding.myapplication.githubappwithapi.background.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavUserDao {
    @Insert
    suspend fun addToFav(favUser: FavUser)

    @Query("SELECT * FROM favorite_user")
    fun getFavUser(): LiveData<List<FavUser>>

    @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.id = :id")
    suspend fun checkUser(id: Int): Int

    @Query("DELETE FROM favorite_user WHERE favorite_user.id = :id")
    suspend fun removeFromFav(id: Int): Int
}