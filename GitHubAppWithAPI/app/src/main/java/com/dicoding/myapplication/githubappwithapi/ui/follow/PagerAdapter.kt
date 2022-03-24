package com.dicoding.myapplication.githubappwithapi.ui.follow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
class PagerAdapter(
    activity: AppCompatActivity,
    data: Bundle,
) : FragmentStateAdapter(activity) {
    private var fragmentBundle: Bundle = data

    init{
        fragmentBundle = data
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position){
            0 -> fragment = FollowingFragment()
            1 -> fragment = FollowersFragment()

        }
//        fragment?.arguments = Bundle().apply {
//            putInt(FollowersFragment.ARG_SECTION_NUMBER, position + 1)
//        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }
}