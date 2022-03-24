package com.dicoding.myapplication.githubappwithapi.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myapplication.githubappwithapi.R
import com.dicoding.myapplication.githubappwithapi.databinding.FragmentFollowlistBinding
import com.dicoding.myapplication.githubappwithapi.ui.detail.DetailGithubUser
import com.dicoding.myapplication.githubappwithapi.ui.main.UserAdapter
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFollowing
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailGithubUser.EXTRA_USERNAME).toString()

        _binding = FragmentFollowlistBinding.bind(view)




        adapter = UserAdapter()

        binding.apply {
            rvFollowlist.setHasFixedSize(true)
            rvFollowlist.layoutManager = LinearLayoutManager(activity)
            rvFollowlist.adapter = adapter
        }

        showProgressBar(true)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ViewModelFollowing::class.java]
        viewModel.setFollowingList(username)
        viewModel.getFollowingList().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showProgressBar(false)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showProgressBar(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}


