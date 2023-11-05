package com.example.invasivespecies_hackrpi_android

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.PermissionChecker.checkSelfPermission

class CameraFragment : Fragment() {
    private lateinit var captureButton: Button
    private lateinit var imgView: ImageView
    val REQUEST_IMAGE_CAPTURE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        captureButton = view.findViewById(R.id.camera_capture_button)
        imgView = view.findViewById(R.id.camera_img_view)


        captureButton.setOnClickListener() {
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                startActivityForResult(captureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (_: ActivityNotFoundException) {

            }

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imgView.setImageBitmap(imageBitmap)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }



}