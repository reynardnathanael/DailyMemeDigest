package com.reynard.dailymemedigest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_meme.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class AddMemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meme)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var shared: SharedPreferences =
            getSharedPreferences(Global.sharedFile, Context.MODE_PRIVATE)
        val userid = shared.getInt("USERID", 0)

        txtUrlAdd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                var url = txtUrlAdd.text.toString()
                if (url != "") Picasso.get().load(url).into(imgMemePreview)
            }

        })

        btnSubmitAdd.setOnClickListener {
            // create volley
            val q = Volley.newRequestQueue(it.context)

            // create api url
            val url = "${Global.localApi}/add_meme.php"

            val stringRequest = object: StringRequest(
                Request.Method.POST, url,
                // if success...
                Response.Listener {
                    // retrieve success message from api
                    val obj = JSONObject(it)

                    Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show()

                    if (obj.getString("result") == "success") {
                        finish()
                    }

                },
                // if error...
                Response.ErrorListener {
                    Toast.makeText(this, "please check your input data!", Toast.LENGTH_SHORT).show()
                }
            ){
                // injects data to send to API
                override fun getParams(): MutableMap<String, String>? {
                    // collection of data <key, value>
                    var map = HashMap<String, String>()

                    // POST variables
                    map["image_url"] = txtUrlAdd.text.toString()
                    map["top_text"] = txtTopText.text.toString()
                    map["bottom_text"] = txtBottomText.text.toString()
                    map["user_id"] = userid.toString()
                    return map
                }
            }

            q.add(stringRequest)

        }
    }
}