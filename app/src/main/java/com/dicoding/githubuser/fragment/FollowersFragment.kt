package com.dicoding.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.activity.DetailActivity
import com.dicoding.githubuser.activity.UserListActivity
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.databinding.ActivityUserListBinding
import com.dicoding.githubuser.databinding.FragmentFollowersBinding
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.MainViewModel

class FollowersFragment : Fragment() {

    private lateinit var adapter: ListUserAdapter
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()


        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)


        if (username != null) {
            showLoading(true)
            mainViewModel.setFollowers(username)
        }

        mainViewModel.getFollowers().observe(viewLifecycleOwner, {
                follower -> if (follower != null) {
                showLoading(false)
                adapter.setData(follower)
            }
        })

        showRecyclerList(username)
    }

    private fun showRecyclerList(username: String?) {
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUsers.layoutManager = LinearLayoutManager(activity)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val moveDetailIntent= Intent(activity, DetailActivity::class.java)
        moveDetailIntent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(moveDetailIntent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}