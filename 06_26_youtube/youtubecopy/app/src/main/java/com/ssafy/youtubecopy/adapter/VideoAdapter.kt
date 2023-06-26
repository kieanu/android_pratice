package com.ssafy.youtubecopy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.youtubecopy.R
import com.ssafy.youtubecopy.databinding.ItemVideoBinding
import com.ssafy.youtubecopy.model.VideoClass

// 리사이클러 뷰 2개를 같은 어뎁터를 사용할 예정
class VideoAdapter(val callback: (String, String) -> Unit) : ListAdapter<VideoClass, VideoAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: VideoClass) {
            with(ItemVideoBinding.bind(view)) {
                titleTextView.text = item.title
                subTitleTextView.text = item.subtitle

                Glide.with(thumbnailImageView.context)
                    .load(item.thumb) // image url
                    .into(thumbnailImageView)

                view.setOnClickListener {
                    callback(item.sources, item.title)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<VideoClass>() {
            override fun areItemsTheSame(oldItem: VideoClass, newItem: VideoClass): Boolean {
                return oldItem == newItem // future: id
            }

            override fun areContentsTheSame(oldItem: VideoClass, newItem: VideoClass): Boolean {
                return oldItem == newItem
            }

        }
    }


}