package com.dicoding.githubuser

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.dicoding.githubuser.databinding.ActivityUserListBinding

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding

    private lateinit var adapter: UserAdapter
    private lateinit var dataName: Array<String>
    private lateinit var dataUsername: Array<String>
    private lateinit var dataAvatar: TypedArray
    private lateinit var dataLocation: Array<String>
    private lateinit var dataFollowers: TypedArray
    private lateinit var dataFollowing: TypedArray
    private lateinit var dataRepo: TypedArray
    private lateinit var dataCompany: Array<String>
    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listView:ListView = binding.lvList
        adapter = UserAdapter(this)
        listView.adapter = adapter

        prepare()
        addItem()


        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(this@UserListActivity, users[position].name, Toast.LENGTH_SHORT).show()
            val moveDetailIntent= Intent(this@UserListActivity, DetailActivity::class.java)
            moveDetailIntent.putExtra(DetailActivity.EXTRA_USER, users[position])
            startActivity(moveDetailIntent)
        }
    }

    private fun prepare() {
        dataAvatar = resources.obtainTypedArray(R.array.avatar)
        dataName = resources.getStringArray(R.array.name)
        dataFollowers = resources.obtainTypedArray(R.array.followers)
        dataFollowing = resources.obtainTypedArray(R.array.following)
        dataUsername = resources.getStringArray(R.array.username)
        dataRepo = resources.obtainTypedArray(R.array.repository)
        dataCompany = resources.getStringArray(R.array.company)
        dataLocation = resources.getStringArray(R.array.location)
    }

    private fun addItem() {
        for (position in dataName.indices) {
            val user = User(
                dataAvatar.getResourceId(position, -1),
                dataUsername[position],
                dataName[position],
                dataLocation[position],
                dataFollowers.getInt(position, -1),
                dataFollowing.getInt(position, -1),
                dataRepo.getInt(position, -1),
                dataCompany[position]
            )
            users.add(user)
        }
        adapter.users = users
    }
}