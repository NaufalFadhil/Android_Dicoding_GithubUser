package com.dicoding.consumerapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerapp.activity.DetailActivity
import com.dicoding.consumerapp.adapter.ListUserAdapter
import com.dicoding.consumerapp.databinding.FragmentFollowingBinding
import com.dicoding.consumerapp.model.User
import com.dicoding.consumerapp.viewmodel.MainViewModel

class FollowingFragment : Fragment() {

    private lateinit var adapter: ListUserAdapter
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
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
            mainViewModel.setFollowing(username)
        }

        mainViewModel.getFollowing().observe(viewLifecycleOwner, {
                following -> if (following != null) {
            showLoading(false)
            adapter.setData(following)
        }
        })

        showRecyclerList(username)
    }

    private fun showRecyclerList(username: String?) {
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUsers.layoutManager = LinearLayoutManager(activity)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User, position: Int) {
                showSelectedUser(data, position)
            }
        })
    }

    private fun showSelectedUser(user: User, position: Int) {
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