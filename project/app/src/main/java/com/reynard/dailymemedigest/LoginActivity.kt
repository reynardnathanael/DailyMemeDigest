package com.reynard.dailymemedigest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show()

                        // intent to LoginActivity
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
                    map["username"] = txtUsernameLogin.text.toString()
                    map["password"] = txtPassLogin.text.toString()
                    return map
                }
            }

            q.add(stringRequest)
        }
    }
}