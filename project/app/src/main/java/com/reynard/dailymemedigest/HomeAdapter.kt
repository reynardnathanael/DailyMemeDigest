package com.reynard.dailymemedigest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_meme.view.*

class HomeAdapter(val memes: ArrayList<Meme>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    class HomeViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.card_meme, parent, false)
        return HomeViewHolder(v)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val url = memes[position].image_url
        Picasso.get().load(url).into(holder.v.imgMeme)
        holder.v.top_textView.text = memes[position].top_text
        holder.v.bottom_textView.text = memes[position].bottom_text
        holder.v.txtLike.text = memes[position].num_likes.toString() + "likes"
    }

    override fun getItemCount(): Int {
        return memes.size
    }
}