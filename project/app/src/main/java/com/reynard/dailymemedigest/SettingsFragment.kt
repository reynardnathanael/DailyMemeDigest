package com.reynard.dailymemedigest

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_settings.*
import java.io.File


class SettingsFragment : Fragment() {
    val REQUEST_PERMISSIONS=1
    val REQUEST_IMAGE_CAPTURE = 2
    val REQUEST_IMAGE_GALLERY = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSIONS)
            } else {
                val pictureDialog = AlertDialog.Builder(requireContext())
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
                pictureDialog.setItems(pictureDialogItems
                ) { dialog, which ->
                    when (which) {
                        0 -> openGallery()
                        1 -> takePicture()
                    }
                }
                pictureDialog.show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){

        }
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


    companion object {
    }
}