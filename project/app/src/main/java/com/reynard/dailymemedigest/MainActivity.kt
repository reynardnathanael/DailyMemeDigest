package com.reynard.dailymemedigest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //    bottom navbar
    val fragments: ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragments.add(HomeFragment())
        fragments.add(MyCreationFragment())
        fragments.add(LeaderboardFragment())
        fragments.add(SettingsFragment())

        val adapter = MyAdapter(this, fragments)

        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                bottomNav.selectedItemId = bottomNav.menu.getItem(position).itemId
            }
        })

        bottomNav.setOnItemSelectedListener {
            viewPager.currentItem = when(it.itemId){
                R.id.ItemHome -> 0
                R.id.ItemMyCreation -> 1
                R.id.ItemLeaderboard -> 2
                R.id.ItemSettings -> 3
                else -> 0
            }
            true
        }
    }
}