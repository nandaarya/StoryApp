package com.example.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.adapter.LoadingStateAdapter
import com.example.storyapp.ui.adapter.StoryListAdapter
import com.example.storyapp.ui.maps.MapsActivity
import com.example.storyapp.ui.setting.SettingActivity
import com.example.storyapp.ui.upload.UploadStoryActivity
import com.example.storyapp.ui.welcome.WelcomeActivity
import com.example.storyapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvStoryList.layoutManager = layoutManager

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        getData()
        setOptionMenu()
        setFAB()
    }

    private fun getData() {
        val adapter = StoryListAdapter()
        binding.rvStoryList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.storyList.observe(this) {
            adapter.submitData(lifecycle, it)
        }
        showLoading(false)
    }

    private fun setFAB() {
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, UploadStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setOptionMenu() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_logout -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.logout_dialog_title))
                        setMessage(getString(R.string.logout_dialog_message))
                        setPositiveButton(getString(R.string.yes)) { _, _ ->
                            mainViewModel.logout()
                        }
                        setNegativeButton(getString(R.string.no)) { _, _ ->

                        }
                        create()
                        show()
                    }
                    true
                }
                R.id.menu_setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_maps -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
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