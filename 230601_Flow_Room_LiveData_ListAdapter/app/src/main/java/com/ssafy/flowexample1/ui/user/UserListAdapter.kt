package com.ssafy.flowexample1.ui.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.flowexample1.database.UserDto
import com.ssafy.flowexample1.databinding.ItemUserListBinding
import com.ssafy.flowexample1.util.Constants

class UserListAdapter(val context: Context) : ListAdapter<UserDto, UserListAdapter.UserViewHolder>(UserDiffCallback()) {
    var selectedUser: UserDto? = null
    var selectedItem: LinearLayout? = null
    private lateinit var binding: ItemUserListBinding

    inner class UserViewHolder(private val binding: ItemUserListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user : UserDto) {
            binding.name.text = user.name
            Constants.setImage(context, binding.img, user.avatar!!)
            binding.item.setOnClickListener {
                // 이전 셀렉트 지우기
                selectedItem?.isSelected = false
                // 갱신
                selectedItem = binding.root
                selectedUser = user
                binding.root.isSelected = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        binding =ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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