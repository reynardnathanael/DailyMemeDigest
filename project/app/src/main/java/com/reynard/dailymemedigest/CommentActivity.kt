package com.reynard.dailymemedigest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.comment_layout.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommentActivity : AppCompatActivity() {
    var comments: ArrayList<Comment> = ArrayList()
    private lateinit var finalNameInput: String

    fun displayList() {
        val lm: LinearLayoutManager = LinearLayoutManager(this)
        commentRecView.layoutManager = lm
        commentRecView.setHasFixedSize(true)
        commentRecView.adapter = CommentAdapter(comments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // get user avatar img
        var shared: SharedPreferences =
            this.getSharedPreferences(Global.sharedFile, Context.MODE_PRIVATE)
        val uname = shared.getString("USERNAME", "")

        val avatar = shared.getString("AVATAR", "")
        Picasso.get().load(avatar).into(commentImg)

        val memeAvatar = intent.getStringExtra(HomeAdapter.MEMEAVATAR)
        Picasso.get().load(memeAvatar).into(imgUser_comment)

        txtUsername_comment.text = intent.getStringExtra(HomeAdapter.MEMEUSERNAME)

        val memeImg = intent.getStringExtra(HomeAdapter.MEMEIMG)
        Picasso.get().load(memeImg).into(imgMeme_comment)

        toptext_comment.text = intent.getStringExtra((HomeAdapter.TOPTEXT))
        bottomtext_comment.text = intent.getStringExtra((HomeAdapter.BOTTOMTEXT))

        //create volley
        val q = Volley.newRequestQueue(this)

        // create api url
        val url = "${Global.api}/get_comment.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            // if success...
            {
                Log.d("comment", it)
                // retrieve success message from api
                val obj = JSONObject(it)

                if (obj.getString("result") == "success") {
                    val data = obj.getJSONArray("data")
                    Log.d("commentLength", data.length().toString())
                    for (i in 0 until data.length()) {
                        val commentObj = data.getJSONObject(i)

                        // change firstname and lastname to char array
                        val firstname = commentObj.getString("firstname").toCharArray()
                        val lastname = commentObj.getString("lastname").toCharArray()
                        var finalName = ""

                        // check if user is private or not
                        if (commentObj.getInt("privacy_setting") == 0) {
                            // if not, just assign the firstname and lastname to finalName
                            finalName =
                                commentObj.getString("firstname") + " " + commentObj.getString("lastname")
                        } else {
                            // if yes, then run this logic to display only the first 3 char
                            var count = 3 // counter

                            // loop for the first char array (firstname)
                            for (i in firstname) {
                                if (count > 0) {
                                    finalName += i
                                    count--
                                } else {
                                    finalName += "*"
                                }
                            }

                            finalName += " "

                            // loop for the second char array (firstname)
                            for (i in lastname) {
                                if (count > 0) {
                                    finalName += i
                                    count--
                                } else {
                                    finalName += "*"
                                }
                            }
                        }

                        finalNameInput = finalName

                        val comment = Comment(
                            commentObj.getInt("comment_id"),
                            commentObj.getInt("user_id"),
                            commentObj.getInt("meme_id"),
                            commentObj.getString("content"),
                            commentObj.getString("comment_date"),
                            commentObj.getString("username"),
                            commentObj.getString("avatar_img"),
                            finalName
                        )
                        comments.add(comment)
                    }
                }

                displayList()
            },
            // if error...
            {
                Toast.makeText(
                    this,
                    "Sorry There's an Error in our system!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ) {
            // injects data to send to API
            override fun getParams() = hashMapOf(
                "meme_id" to intent.getStringExtra(HomeAdapter.MEMEID).toString()
            )
        }
        q.add(stringRequest)

        btnPost.setOnClickListener {
            // get Now
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var dateNow = sdf.format(calendar.time)

            //create volley
            val q2 = Volley.newRequestQueue(it.context)

            // create api url
            val url = "${Global.api}/add_comment.php"

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                // if success...
                {
                    Log.d("addComment", it)
                    val comment = Comment(
                        comments.get(comments.size - 1).comment_id,
                        intent.getStringExtra(HomeAdapter.MEMEID).toString().toInt(),
                        intent.getStringExtra(HomeAdapter.USERID).toString().toInt(),
                        txtNewComment.text.toString(),
                        dateNow,
                        uname.toString(),
                        avatar.toString(),
                        finalNameInput.toString()
                    )
                    comments.add(comment)
                    displayList()
                    txtNewComment.setText("")
                },
                // if error...
                {
                    Toast.makeText(
                        this,
                        "Sorry There's an Error in our system!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                // injects data to send to API
                override fun getParams() = hashMapOf(
                    "user_id" to intent.getStringExtra(HomeAdapter.USERID).toString(),
                    "meme_id" to intent.getStringExtra(HomeAdapter.MEMEID).toString(),
                    "content" to txtNewComment.text.toString(),
                    "comment_date" to dateNow
                )
            }
            q2.add(stringRequest)
        }
    }
}