package com.reynard.dailymemedigest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject

class HomeFragment : Fragment() {
    var memes: ArrayList<Meme> = ArrayList()
    var order = "m.meme_id";

    fun displayList(user_id: String) {
        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.homeView)
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = HomeAdapter(memes, user_id)
    }

    // add data to ArrayList memes from the API
    fun addData(order_by: String) {
        memes.clear()
        // get username from SharedPreferences
        var shared: SharedPreferences =
            requireActivity().getSharedPreferences(Global.sharedFile, Context.MODE_PRIVATE)
        val userId = shared.getInt("USERID", 0)

        // create volley
        var q = Volley.newRequestQueue(activity)

        // create api url
        val url = "${Global.api}/home_meme.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            // if success...
            {
                Log.d("home", it)
                // retrieve success message from api
                val obj = JSONObject(it)

                if (obj.getString("result") == "success") {
                    val data = obj.getJSONArray("data")
                    Log.d("length", data.length().toString())
                    for (i in 0 until data.length()) {
                        val memeObj = data.getJSONObject(i)
                        val meme = Meme(
                            memeObj.getInt("meme_id"),
                            memeObj.getString("image_url"),
                            memeObj.getString("top_text"),
                            memeObj.getString("bottom_text"),
                            memeObj.getInt("num_likes"),
                            memeObj.getString("username"),
                            memeObj.getString("avatar_img")
                        )
                        memes.add(meme)
                    }
                }

                displayList(userId.toString())
            },
            // if error...
            {
                Toast.makeText(
                    requireContext(),
                    "Sorry There's an Error in our system",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ){
            // injects data to send to API
            override fun getParams(): MutableMap<String, String>? {
                // collection of data <key, value>
                var map = HashMap<String, String>()

                // POST variables
                map["order"] = order_by
                return map
            }
        }

        q.add(stringRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addData(order);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddMemeActivity::class.java)
            startActivity(intent)
        }

        groupFilter.setOnCheckedChangeListener { radioGroup, id ->
            if(id == R.id.rdoComments) {
                order = "count(mc.comment_id)"
            }
            else if(id == R.id.rdoLike) {
                order = "m.num_likes"
            }
            else if(id == R.id.rdoDate) {
                order = "m.meme_id"

            }
            else if(id == R.id.rdoView) {
                order = "m.num_views";
            }
            addData(order);
        }
    }
}