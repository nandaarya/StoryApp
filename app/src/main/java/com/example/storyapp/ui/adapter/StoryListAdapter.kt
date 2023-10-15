package com.example.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.ItemLayoutBinding

class StoryListAdapter(private val storyList: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryListAdapter.StoryListViewHolder>() {
    inner class StoryListViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem) {
            binding.tvName.text = storyItem.name
            binding.tvDescription.text = storyItem.description

            Glide
                .with(itemView.context)
                .load(storyItem.photoUrl)
                .fitCenter()
                .into(binding.ivProfilePhoto)

//            binding.itemLayout.setOnClickListener {
//                val intent = Intent(itemView.context, DetailUserActivity::class.java)
//                intent.putExtra("username", githubUser.login)
//                itemView.context.startActivity(intent)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryListViewHolder {
        return StoryListViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    override fun onBindViewHolder(holder: StoryListViewHolder, position: Int) {
        val storyItem = storyList[position]
        holder.bind(storyItem)
    }
}