package com.example.invasivespecies_hackrpi_android

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

import kotlin.io.path.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString


class CameraFragment : Fragment() {
    private lateinit var view: View
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
        this.view = view
//        captureButton = view.findViewById(R.id.camera_capture_button)
        imgView = view.findViewById(R.id.camera_img_view)


//        captureButton.setOnClickListener() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            startActivityForResult(captureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (_: ActivityNotFoundException) {

        }

//        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            imgView.setImageBitmap(imageBitmap)
            var resized_bitmap = Bitmap.createScaledBitmap(imageBitmap, 224, 224, false)
            var module = LiteModuleLoader.load(assetFilePath(requireActivity().applicationContext!!, "model.ptl"))
            val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resized_bitmap,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);
            val outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
            val scores = outputTensor.dataAsFloatArray.toTypedArray();
            // searching for the index with maximum score

            // searching for the index with maximum score
            var maxScore = -Float.MAX_VALUE
            var maxScoreIdx = -1

            val sortedScores = scores.sorted()
            var rankedString: String = ""
            for (i in 1 until 6) {
                rankedString += "#" + i + " : " + ImageNetClasses.CLASSES[scores.indexOf(sortedScores[i-1])] + " : " + ((sortedScores[i-1] * -100).toInt()) + "%\n"
//                if (scores[i] > maxScore) {
//                    maxScore = scores[i]
//                    maxScoreIdx = i
//                }
            }

//            Log.d("src", "" + maxScoreIdx)
//            val className: String = ImageNetClasses.CLASSES[maxScoreIdx]

            // showing className on UI

            // showing className on UI
            val textView: TextView = this.view.findViewById(R.id.textViewResults)
            textView.text = ("Predictions:\n\n" + rankedString)

        }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
    }

    fun assetFilePath(context: Context, asset: String): String {
        val file = File(context.filesDir, asset)

        try {
            val inpStream: InputStream = context.assets.open(asset)
            try {
                val outStream = FileOutputStream(file, false)
                val buffer = ByteArray(4 * 1024)
                var read: Int

                while (true) {
                    read = inpStream.read(buffer)
                    if (read == -1) {
                        break
                    }
                    outStream.write(buffer, 0, read)
                }
                outStream.flush()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

}