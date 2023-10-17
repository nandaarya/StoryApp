package com.example.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.ItemLayoutBinding
import com.example.storyapp.ui.detail.DetailActivity

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
                .into(binding.ivStoryImage)

            binding.itemLayout.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("storyItem", storyItem)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivStoryImage, "story_image"),
                        Pair(binding.tvName, "name"),
                        Pair(binding.tvDescription, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
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