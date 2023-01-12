package com.reynard.dailymemedigest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.drawer_header.view.*

class MainActivity : AppCompatActivity() {
    // drawer
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle

    //  navbar
    val fragments: ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer)
//        setContentView(R.layout.activity_main)

        var shared: SharedPreferences =
            this.getSharedPreferences(Global.sharedFile, Context.MODE_PRIVATE)
        val username = shared.getString("USERNAME", "")
        val avatarImg = shared.getString("AVATAR", "")
        val name = shared.getString("FIRSTNAME", "") + " " + shared.getString("LASTNAME", "")

        // bottom Navbar
        fragments.add(HomeFragment())
        fragments.add(MyCreationFragment())
        fragments.add(LeaderboardFragment())
        fragments.add(SettingsFragment())

        val adapter = MyAdapter(this, fragments)

        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNav.selectedItemId = bottomNav.menu.getItem(position).itemId
            }
        })

        bottomNav.setOnItemSelectedListener {
            viewPager.currentItem = when (it.itemId) {
                R.id.ItemHome -> 0
                R.id.ItemMyCreation -> 1
                R.id.ItemLeaderboard -> 2
                R.id.ItemSettings -> 3
                else -> 0
            }
            true
        }

        // drawer Navbar
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // set supaya ada back button di drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // set data diri user
        var headerView: View = navigationView.getHeaderView(0)

        Picasso.get().load(avatarImg).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
            .into(headerView.drawerImg)
        headerView.drawer_username.text = username
        headerView.drawer_name.text = name

        // nav_view ada di drawer
        // kalau item di nav_view di select akan menuju ke fragment tersebut
        navigationView.setNavigationItemSelectedListener {
            viewPager.currentItem = when (it.itemId) {
                R.id.ItemHome -> 0
                R.id.ItemMyCreation -> 1
                R.id.ItemLeaderboard -> 2
                R.id.ItemSettings -> 3
                else -> 0
            }
            true
        }

        headerView.fabLogout.setOnClickListener {
            // remove sharedPreferences
            shared.edit().clear().apply()

            // intent to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }


    }

    public fun updateDrawer() {
        var shared: SharedPreferences =
            this.getSharedPreferences(Global.sharedFile, Context.MODE_PRIVATE)
        var headerView: View = navigationView.getHeaderView(0)
        val avatarImg = shared.getString("AVATAR", "")
        val name = shared.getString("FIRSTNAME", "") + " " + shared.getString("LASTNAME", "")
        Picasso.get().load(avatarImg).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
            .into(headerView.drawerImg)
        headerView.drawer_name.text = name
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}