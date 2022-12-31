package com.reynard.dailymemedigest

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_meme.view.*
import org.json.JSONObject

class HomeAdapter(val memes: ArrayList<Meme>, val user_id: String) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    class HomeViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    companion object{
        val USERID = "USERID"
        val MEMEID = "MEMEID"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.card_meme, parent, false)
        return HomeAdapter.HomeViewHolder(v)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        // for user
        val urlUser = memes[position].avatar_img
        Picasso.get().load(urlUser).into(holder.v.memeUserImg)
        holder.v.userView.text = memes[position].username

        // for meme
        val url = memes[position].image_url
        Picasso.get().load(url).into(holder.v.imgMeme)
        holder.v.top_textView.text = memes[position].top_text
        holder.v.bottom_textView.text = memes[position].bottom_text
        holder.v.txtLike.text = memes[position].num_likes.toString() + " likes"

        // for like
        holder.v.btnLike.setOnClickListener {
            // create volley
            val q = Volley.newRequestQueue(it.context)

            // create api url
            val url = "${Global.localApi}/set_likes.php"

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                // if success...
                {
                    Log.d("like", it)
                    val obj = JSONObject(it)

                    when (obj.getString("message")) {
                        "like" -> memes[holder.adapterPosition].num_likes++
                        "dislike" -> memes[holder.adapterPosition].num_likes--
                    }
                    var newlikes = memes[holder.adapterPosition].num_likes

                    holder.v.txtLike.text = "$newlikes likes"
                },
                Response.ErrorListener {
                    Log.d("like", it.message.toString())
                    Toast.makeText(
                        holder.v.context,
                        "Sorry There's an Error in our system",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                override fun getParams() = hashMapOf(
                    "user_id" to user_id,
                    "meme_id" to memes[holder.adapterPosition].meme_id.toString()
                )
            }
            q.add(stringRequest)
        }

        holder.v.btnComment.setOnClickListener {
            val commentIntent = Intent(holder.v.context, CommentActivity::class.java)
            commentIntent.putExtra(USERID, user_id)
            commentIntent.putExtra(MEMEID, memes[holder.adapterPosition].meme_id.toString())
            holder.v.context.startActivity(commentIntent)
        }
    }

    override fun getItemCount(): Int {
        return memes.size
    }
}