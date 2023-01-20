package com.example.finalsproject

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.finalsproject.fragments.TodoAddFragment
import com.example.finalsproject.fragments.TodoListFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2



    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return TodoListFragment()
        }
        return TodoAddFragment()
    }
}