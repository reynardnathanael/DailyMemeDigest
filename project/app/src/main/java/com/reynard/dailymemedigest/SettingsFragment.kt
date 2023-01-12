package com.reynard.dailymemedigest

import android.Manifest
import android.R.attr.bitmap
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_meme.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SettingsFragment : Fragment() {
    val REQUEST_PERMISSIONS = 1
    val REQUEST_IMAGE_CAPTURE = 2
    val REQUEST_IMAGE_GALLERY = 3
    var userid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var shared:SharedPreferences=requireActivity().getSharedPreferences(Global.sharedFile, Context.MODE_PRIVATE)
        userid = shared.getInt("USERID", 0)
        val username = shared.getString("USERNAME", "")
        val firstname = shared.getString("FIRSTNAME", "")
        var lastname = shared.getString("LASTNAME", "")
        val registdate = shared.getString("REGISTDATE", "")
        val privacy = shared.getInt("PRIVACY", 0)
        val avatar = shared.getString("AVATAR", "")
        if (lastname == "null") {
            lastname = ""
        }
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("MMM yyyy")
        val date = formatter.format(parser.parse(registdate))

        txtNameSettings.text = "$firstname $lastname"
        txtRegisterDateSettings.text = "Active since $date"
        txtUsernameSettings.text = username
        txtFirstNameSettings.setText(firstname)
        txtLastNameSettings.setText(lastname)
        if (privacy === 1) {
            checkHide.isChecked = true
        }
        Picasso.get().load(avatar).into(imgAvatarSettings)
        imgAvatarSettings.setOnClickListener {
            Log.d("test", "button")
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), REQUEST_PERMISSIONS
                )
            } else {
                chooseIntent()
            }
        }
        btnSaveSettings.setOnClickListener {
            // create volley
            val q = Volley.newRequestQueue(it.context)

            // create api url
            val url = "${Global.api}/update_profile.php"

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                // if success...
                Response.Listener {
                    // retrieve success message from api
                    val obj = JSONObject(it)

                    Toast.makeText(requireContext(), obj.getString("message"), Toast.LENGTH_SHORT)
                        .show()

                    if (obj.getString("result") == "success") {
                        var privacy=0
                        if (checkHide.isChecked) {
                            privacy=1
                        }
                        // put all the user's data to SharedPreferences
                        var editor: SharedPreferences.Editor = shared.edit()
                        editor.putString("FIRSTNAME", txtFirstNameSettings.text.toString())
                        editor.putString("LASTNAME", txtLastNameSettings.text.toString())
                        editor.putInt("PRIVACY", privacy)

                        editor.apply()
                    }

                },
                // if error...
                Response.ErrorListener {
                    Toast.makeText(
                        requireContext(),
                        "please check your input data!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                // injects data to send to API
                override fun getParams(): MutableMap<String, String>? {
                    // collection of data <key, value>
                    var map = HashMap<String, String>()

                    // POST variables
                    map["firstname"] = txtFirstNameSettings.text.toString()
                    map["lastname"] = txtLastNameSettings.text.toString()
                    if (checkHide.isChecked) {
                        map["privacy_setting"] = "1"
                    } else {
                        map["privacy_setting"] = "0"
                    }
                    map["user_id"] = userid.toString()
                    return map
                }
            }

            q.add(stringRequest)

        }
        fabLogoutSettings.setOnClickListener {
            shared.edit().clear().apply()

            // intent to LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

            activity?.finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    chooseIntent()
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imageBitmap: Bitmap? = null
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            imageBitmap = MediaStore.Images.Media.getBitmap(
                requireActivity().getContentResolver(),
                data?.data
            )
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data!!.extras
            imageBitmap = extras!!.get("data") as Bitmap
        }
        imgAvatarSettings.setImageBitmap(imageBitmap)
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)

        // create volley
        val q = Volley.newRequestQueue(this.context)

        // create api url
        val url = "${Global.api}/update_avatar.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            // if success...
            Response.Listener {
                // retrieve success message from api
                val obj = JSONObject(it)

                var shared:SharedPreferences=requireActivity().getSharedPreferences(Global.sharedFile, Context.MODE_PRIVATE)
                var editor: SharedPreferences.Editor = shared.edit()
                editor.putString("AVATAR", "https://ubaya.fun/native/160720034/memes_api/img_profile/${userid}.jpg")

                editor.apply()

                Toast.makeText(requireContext(), obj.getString("message"), Toast.LENGTH_SHORT)
                    .show()

            },
            // if error...
            Response.ErrorListener {
                Toast.makeText(
                    requireContext(),
                    "please check your input data!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ) {
            // injects data to send to API
            override fun getParams(): MutableMap<String, String>? {
                // collection of data <key, value>
                var map = HashMap<String, String>()

                // POST variables
                map["avatar_img"] = base64
                map["user_id"] = userid.toString()

                return map
            }
        }

        q.add(stringRequest)
    }

    fun takePicture() {
        val i = Intent()
        i.action = MediaStore.ACTION_IMAGE_CAPTURE
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE)
    }

    fun openGallery() {
        val i = Intent()
        i.action = Intent.ACTION_GET_CONTENT
        i.setType("image/*")
        startActivityForResult(i, REQUEST_IMAGE_GALLERY)
    }

    fun chooseIntent() {
        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems =
            arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> openGallery()
                1 -> takePicture()
            }
        }
        pictureDialog.show()
    }


    companion object {
    }
}