package com.ssafy.fbrealdbexample

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.fbrealdbexample.database.UserDto
import com.ssafy.fbrealdbexample.databinding.ItemUserBinding
import com.ssafy.flowexample1.util.Constants

class UserListAdapter(val context: Context) : ListAdapter<UserDto, UserListAdapter.UserViewHolder>(UserDiffCallback()) {
    private lateinit var binding: ItemUserBinding

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user : UserDto) {
            binding.name.text = user.name
            binding.age.text = user.age.toString()
            binding.introduce.text = user.introduce
            Constants.setImage(context, binding.img, user.avatar!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        binding =ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<UserDto>() {
        override fun areItemsTheSame(oldItem: UserDto, newItem: UserDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserDto, newItem: UserDto): Boolean {
            return oldItem == newItem
        }
    }
}