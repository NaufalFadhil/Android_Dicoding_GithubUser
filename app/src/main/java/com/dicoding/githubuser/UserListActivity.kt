package com.dicoding.githubuser

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

class UserListActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var dataName: Array<String>
    private lateinit var dataDescription: Array<String>
    private lateinit var dataPhoto: TypedArray
    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val listView:ListView = findViewById(R.id.lv_list)
        adapter = UserAdapter(this)
        listView.adapter = adapter

        prepare()
        addItem()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(this@UserListActivity, users[position].name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun prepare() {
        dataName = resources.getStringArray(R.array.name)
        dataDescription = resources.getStringArray(R.array.location)
        dataPhoto = resources.obtainTypedArray(R.array.avatar)
    }

    private fun addItem() {
        for (position in dataName.indices) {
            val hero = User(
                dataPhoto.getResourceId(position, -1),
                dataName[position],
                dataDescription[position]
            )
            users.add(hero)
        }
        adapter.users = users
    }
}