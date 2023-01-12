package com.reynard.dailymemedigest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comment_layout.view.*

class CommentAdapter(val comments: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    class CommentViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.comment_layout, parent, false)
        return CommentAdapter.CommentViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val url = comments[position].avatar_img
        Picasso.get().load(url).into(holder.v.userImg_comment)

        holder.v.txtComment_comment.text = comments[position].content
//        holder.v.txtUser_comment.text = comments[position].username
        holder.v.txtUser_comment.text = comments[position].finalName
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}