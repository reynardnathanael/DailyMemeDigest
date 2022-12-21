package com.reynard.dailymemedigest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // declare SharedPreferences
        var sharedFile = "com.reynard.dailymemedigest"
        var shared: SharedPreferences = getSharedPreferences(sharedFile, Context.MODE_PRIVATE)

        val username = shared.getString("USERNAME", "false")
        val password = shared.getString("PASSWORD", "false")

        // check if user has logged out or not by checking the SharedPreferences
        if (username == "false" && password == "false") {
            btnCreateAcc.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)

                finish()
            }

            btnLogin.setOnClickListener {
                // create volley
                val q = Volley.newRequestQueue(it.context)

                // create url options (localhost or hosting)
                val local = "http://10.0.2.2/DailyMemeDigest/api/"
                val host = "http://ubaya.fun/native/160720034/memes_api/"

                // create api url
                val url = "$local/login_process.php"

                val stringRequest = object: StringRequest(
                    Request.Method.POST, url,
                    // if success...
                    Response.Listener {
                        // retrieve success message from api
                        val obj = JSONObject(it)

                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show()

                        if (obj.getString("result") == "success") {
                            // retrieve JSON object named "data"
                            val data = obj.getJSONObject("data")

                            // put all the user's data to SharedPreferences
                            var editor:SharedPreferences.Editor = shared.edit()
                            editor.putInt("USERID", data.getInt("user_id"))
                            editor.putString("FIRSTNAME", data.getString("firstname"))
                            editor.putString("LASTNAME", data.getString("lastname"))
                            editor.putString("USERNAME", data.getString("username"))
                            editor.putString("PASSWORD", data.getString("password"))
                            editor.putString("REGISTDATE", data.getString("registration_date"))
                            editor.putString("AVATAR", data.getString("avatar_img"))
                            editor.putInt("PRIVACY", data.getInt("privacy_setting"))

                            editor.apply()

                            // intent to MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)

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
                        map["username"] = txtFirstNameSettings.text.toString()
                        map["password"] = txtPassLogin.text.toString()
                        return map
                    }
                }

                q.add(stringRequest)
            }
        }

        else {
            // intent to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}