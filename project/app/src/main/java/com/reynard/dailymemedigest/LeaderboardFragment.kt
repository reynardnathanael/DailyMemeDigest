package com.reynard.dailymemedigest

import android.os.Bundle
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
import org.json.JSONObject

class LeaderboardFragment : Fragment() {
    var leadeboards: ArrayList<Leaderboards> = ArrayList()

    fun displayList() {
        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.recViewLeaderboard)
        recyclerView?.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = LeaderboardAdapter(leadeboards)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create volley
        var q = Volley.newRequestQueue(activity)

        // create api url
        val url = "${Global.api}/leaderboards.php"

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

                        // change firstname and lastname to char array
                        val firstname = leadObj.getString("firstname").toCharArray()
                        val lastname = leadObj.getString("lastname").toCharArray()
                        var finalName = ""

                        // check if user is private or not
                        if (leadObj.getInt("privacy_setting") == 0) {
                            // if not, just assign the firstname and lastname to finalName
                            finalName = leadObj.getString("firstname") + " " + leadObj.getString("lastname")
                        }
                        else {
                            // if yes, then run this logic to display only the first 3 char
                            var count = 3 // counter

                            // loop for the first char array (firstname)
                            for (i in firstname) {
                                if (count > 0) {
                                    finalName += i
                                    count--
                                }
                                else {
                                    finalName += "*"
                                }
                            }

                            finalName += " "

                            // loop for the second char array (firstname)
                            for (i in lastname) {
                                if (count > 0) {
                                    finalName += i
                                    count--
                                }
                                else {
                                    finalName += "*"
                                }
                            }
                        }

                        val leaderboard = Leaderboards(
                            finalName,
                            leadObj.getString("avatar_img"),
                            leadObj.getInt("jumlah")
                        )
                        leadeboards.add(leaderboard)
                    }
                }

                displayList()

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