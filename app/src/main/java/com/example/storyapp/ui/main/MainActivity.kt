package com.example.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.Result
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.adapter.StoryListAdapter
import com.example.storyapp.ui.welcome.WelcomeActivity
import com.example.storyapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryListAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvStoryList.layoutManager = layoutManager

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                Log.d("story list","token di mainActivity: ${user.token}")
                mainViewModel.getStories()
            }
        }

        mainViewModel.storyList.observe(this) {
            when (it) {
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    showLoading(false)
                    Log.d("story list","data story list gagal diambil")
                }
                is Result.Success -> {
                    showLoading(false)
                    Log.d("story list","data story list di passing ke adapter")
                    adapter = StoryListAdapter(it.data)
                    binding.rvStoryList.adapter = adapter
                }
            }
        }

        setOptionMenu()
        Log.d("story list","akhir on create")
    }

    private fun setOptionMenu() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_logout -> {
                    mainViewModel.logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvStoryList.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}