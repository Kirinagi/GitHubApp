package com.dicoding.myapplication.githubappwithapi.background.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(
    entities = [FavUser::class],
    version = 1
)
abstract class GitUserDatabase: RoomDatabase() {
    @InternalCoroutinesApi
    companion object{
        private var INSTANCE : GitUserDatabase? = null

        fun getDatabase(context: Context): GitUserDatabase?{
            if(INSTANCE ==null){
                synchronized(GitUserDatabase::class){
                    INSTANCE = databaseBuilder(context.applicationContext, GitUserDatabase::class.java,"user_database").build()
                }
            }
            return INSTANCE
        }
    }
    abstract fun favUserDao(): FavUserDao
}