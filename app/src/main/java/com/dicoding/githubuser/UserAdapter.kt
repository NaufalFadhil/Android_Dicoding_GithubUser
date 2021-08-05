package com.dicoding.githubuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.dicoding.githubuser.databinding.ItemUserBinding

class UserAdapter internal constructor(private val context: Context) : BaseAdapter() {
    internal var users = arrayListOf<User>()
    override fun getCount(): Int = users.size

    override fun getItem(position: Int): Any = users[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        }

        val viewHolder = ViewHolder(itemView as View)

        val user = getItem(position) as User
        viewHolder.bind(user)
        return itemView
    }

    private inner class ViewHolder(view: View) {
        private val binding = ItemUserBinding.bind(view)

        fun bind(user: User) {
            binding.txtName.text = user.name
            binding.txtFollowers.text = user.followers.toString()
            binding.txtFollowing.text = user.following.toString()
            binding.imgAvatar.setImageResource(user.avatar)
        }
    }
}