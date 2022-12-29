package com.reynard.dailymemedigest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LeaderboardFragment : Fragment() {
    var leadeboards: ArrayList<Leaderboards> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create volley
        var q = Volley.newRequestQueue(activity)

        // create api url
        val url = "${Global.localApi}/leaderboards.php"

        val stringRequest = StringRequest(
            Request.Method.POST, url,
            // if success...
            {
                // retrieve success message from api
                val obj = JSONObject(it)

                if (obj.getString("result") == "success") {
                    val data = obj.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val leadObj = data.getJSONObject(i)
                        val leaderboard = Leaderboards(
                            leadObj.getString("firstname"),
                            leadObj.getString("lastname"),
                            leadObj.getString("avatar_img"),
                            leadObj.getInt("jumlah")
                        )
                        leadeboards.add(leaderboard)
                    }
                }

            },
            // if error...
            {
                Toast.makeText(
                    requireContext(),
                    "Sorry There's an Error in our system",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        q.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }
}