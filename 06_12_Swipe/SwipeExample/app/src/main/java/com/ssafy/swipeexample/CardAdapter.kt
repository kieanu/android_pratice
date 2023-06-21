package com.ssafy.swipeexample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.swipeexample.databinding.FragmentItemBinding

class CardAdapter() : ListAdapter<CardItem, CardAdapter.CardViewHolder>(CardDiffCallback()){
    private lateinit var binding : FragmentItemBinding

    // ViewHolder 클래스
    inner class CardViewHolder(private val binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cardItem: CardItem) {
            // swipe
            binding.cardItem.setOnClickListener {  }
            binding.cardText.text = cardItem.desc
            binding.cardImg.setImageResource(cardItem.imgSrc)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        binding = FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // DiffUtil.ItemCallback 클래스
    private class CardDiffCallback:DiffUtil.ItemCallback<CardItem>() {

        override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
            return oldItem == newItem
        }
    }
}