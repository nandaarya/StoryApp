package com.example.storyapp.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var storyItem: ListStoryItem
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_page_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        storyItem = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("storyItem", ListStoryItem::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("storyItem")!!
        }

        setData()
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setData() {
        Glide
            .with(this)
            .load(storyItem.photoUrl)
            .fitCenter()
            .into(binding.ivProfilePhoto)

        binding.tvName.text = storyItem.name
        binding.tvDescription.text = storyItem.description
    }
}