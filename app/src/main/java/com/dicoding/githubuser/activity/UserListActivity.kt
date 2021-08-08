package com.dicoding.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.User
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.databinding.ActivityUserListBinding

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsers.setHasFixedSize(true)

        list.addAll(getListUsers())
        showRecyclerList()

//        val listView:ListView = binding.lvList
//        adapter = UserAdapter(this)
//        listView.adapter = adapter
//
//        prepare()
//        addItem()
//
//        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
//            Toast.makeText(this@UserListActivity, users[position].name, Toast.LENGTH_SHORT).show()
//            val moveDetailIntent= Intent(this@UserListActivity, DetailActivity::class.java)
//            moveDetailIntent.putExtra(DetailActivity.EXTRA_USER, users[position])
//            startActivity(moveDetailIntent)
//        }
    }

    fun getListUsers(): ArrayList<User> {
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)
        val dataName = resources.getStringArray(R.array.name)
        val dataFollowers = resources.getStringArray(R.array.followers)
        val dataFollowing = resources.getStringArray(R.array.following)

        val listUser = ArrayList<User>()
        for (position in dataName.indices) {
            val user = User (
                dataAvatar.getResourceId(position, -1),
                dataName[position],
                dataFollowers[position],
                dataFollowing[position],
//                dataUsername[position],
//                dataLocation[position],
//                dataRepo.getInt(position, -1),
//                dataCompany[position]
            )
            listUser.add(user)
        }
        return listUser
    }

    private fun showRecyclerList() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        binding.rvUsers.adapter = listUserAdapter
    }
}