package com.reynard.dailymemedigest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        txtPassRegist.transformationMethod = PasswordTransformationMethod.getInstance()
        txtRepassRegist.transformationMethod = PasswordTransformationMethod.getInstance()

        btnRegister.setOnClickListener {
            // check password
            if (txtPassRegist.text.toString() != txtRepassRegist.text.toString()) {
                Toast.makeText(this, "Password and Repeat Password must be match!", Toast.LENGTH_SHORT).show()
            }
            else {
                // create volley
                val q = Volley.newRequestQueue(it.context)

                // create api url
                val url = "${Global.api}/register.php"

                val stringRequest = object: StringRequest(
                    Request.Method.POST, url,
                    // if success...
                    Response.Listener {
                        // retrieve success message from api
                        val obj = JSONObject(it)
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show()

                        if (obj.getString("result") == "success") {
                            // intent to LoginActivity
                            val intent = Intent(this, LoginActivity::class.java)
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
                        map["firstname"] = txtFirstName.text.toString()
                        map["lastname"] = txtLastName.text.toString()
                        map["username"] = txtUsernameRegist.text.toString()
                        map["password"] = txtPassRegist.text.toString()
                        return map
                    }
                }

                q.add(stringRequest)
            }
        }
    }
}