package com.reynard.dailymemedigest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            // create volley
            val q = Volley.newRequestQueue(it.context)

            // create choice url (localhost or hosting)
            val local = "http://10.0.2.2/DailyMemeDigest/api/"
            val host = "http://ubaya.fun/native/160720034/memes_api/"

            // create url
            val url = local + "register.php"

            val stringRequest = object: StringRequest(
                Request.Method.POST, url,
                Response.Listener {
                    val obj = JSONObject(it)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                },
                Response.ErrorListener {
                    Toast.makeText(this, "please check your input data!", Toast.LENGTH_SHORT).show()
                }
            ){
                override fun getParams(): MutableMap<String, String>? {
                    var map = HashMap<String, String>()
                    map["username"] = txtUsernameRegist.text.toString()
                    map["password"] = txtPassRegist.text.toString()
                    return map
                }
            }

            q.add(stringRequest)
        }
    }
}