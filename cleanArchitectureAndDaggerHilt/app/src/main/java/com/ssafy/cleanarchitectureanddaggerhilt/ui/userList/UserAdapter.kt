package com.ssafy.cleanarchitectureanddaggerhilt.ui.userList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ssafy.cleanarchitectureanddaggerhilt.databinding.ItemUserBinding
import com.ssafy.cleanarchitectureanddaggerhilt.domain.model.User
import com.ssafy.cleanarchitectureanddaggerhilt.ui.base.BaseAdapter
import javax.inject.Inject

class UserAdapter @Inject constructor(
    private val glide: RequestManager
) : BaseAdapter<User>() {

    private val diffCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    override val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root), Binder<User>
    {
        override fun bind(user: User) {
            binding.apply {
                textViewName.text = user.name
                textViewGender.text = user.gender
                textViewId.text = user.id.toString()
                glide.load(user.profileImage).into(imageView)
                root.setOnClickListener {
                    onItemClickListener?.let { itemClick ->
                        itemClick(user)
                    }
                }
            }
        }
    }
}
