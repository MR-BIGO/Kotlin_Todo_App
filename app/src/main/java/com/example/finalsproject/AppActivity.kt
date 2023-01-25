package com.example.finalsproject

import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalsproject.databinding.ActivityAppBinding
import com.google.android.material.tabs.TabLayoutMediator

class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    private lateinit var reciever: AirplaneModeBroadcastListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reciever = AirplaneModeBroadcastListener()
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(reciever, it)
        }
        val tabLayout = binding.tabLayout
        val viewPager = binding.pager
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            if (position == 0){
                tab.setIcon(R.drawable.ic_baseline_todo_list)
            }
            if (position == 1){
                tab.setIcon(R.drawable.ic_baseline_add_todo)
            }

        }.attach()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(reciever)
    }
}