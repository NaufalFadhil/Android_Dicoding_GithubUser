package com.dicoding.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.databinding.ItemRowUserBinding

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    private val userData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        userData.clear()
        userData.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    var listFavorite = ArrayList<User>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listNotes)
            notifyDataSetChanged()
        }

    fun addItem(favorite: User) {
        this.listFavorite.add(favorite)
        notifyItemInserted(this.listFavorite.size - 1)
    }
    fun updateItem(position: Int, favorite: User) {
        this.listFavorite[position] = favorite
        notifyItemChanged(position, favorite)
    }
    fun removeItem(position: Int) {
        this.listFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorite.size)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(userData[position])
    }

    override fun getItemCount(): Int = userData.size

    inner class ListViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(90,90))
                    .into(imgAvatar)
                binding.tvUsername.text = user.username
                itemView.setOnClickListener{onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}