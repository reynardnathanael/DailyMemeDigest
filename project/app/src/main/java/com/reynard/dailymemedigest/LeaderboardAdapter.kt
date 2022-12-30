package com.reynard.dailymemedigest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_meme.view.*
import kotlinx.android.synthetic.main.leaderboards_list.view.*

class LeaderboardAdapter(val leaderboards: ArrayList<Leaderboards>) : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {
    class LeaderboardViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.leaderboards_list, parent, false)
        return LeaderboardAdapter.LeaderboardViewHolder(v)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val url = leaderboards[position].avatar_img
        Picasso.get().load(url).into(holder.v.imgLeaderboard)
        holder.v.txtNameLeaderboard.text = leaderboards[position].name
        holder.v.txtLikeLeaderboard.text = leaderboards[position].num_likes.toString()
    }

    override fun getItemCount(): Int {
        return leaderboards.size
    }
}