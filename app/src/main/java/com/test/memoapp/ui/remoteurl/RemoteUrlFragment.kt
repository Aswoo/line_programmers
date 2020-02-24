package com.test.memoapp.ui.remoteurl

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.test.memoapp.MainActivity
import com.test.memoapp.R
import com.test.memoapp.data.Memo
import kotlinx.android.synthetic.main.remote_url_frag.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RemoteUrlFragment : Fragment() {


    private val args: RemoteUrlFragmentArgs by navArgs()

    private lateinit var outputDirectory: File

    val imageUrl =
        "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.remote_url_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outputDirectory = MainActivity.getOutputDirectory(requireContext())

        download_remote_url.setOnClickListener {
            val url = input_remote_url.text.trim().toString()

            if (url != null && url != "") {

                Glide.with(requireContext())
                    .asBitmap()
                    .load(url)
                    .into(target)
            }
        }
        download_remote_url_sample.setOnClickListener {

            Glide.with(requireContext())
                .asBitmap()
                .load(imageUrl)
                .into(target)
        }
    }

    val target = object : SimpleTarget<Bitmap>() {

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {


            val photoFile = createFile(
                outputDirectory,
                FILENAME,
                PHOTO_EXTENSION
            )
            saveBitmapToJpeg(resource, photoFile)
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            // handle exception
            Toast.makeText(context, R.string.image_url_load_error, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveBitmapToJpeg(bitmap: Bitmap, outputFile: File) {

        try {
            val out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            out.flush()
            out.close()

            val memo: Memo = args.memo

            val mutableList: MutableList<String> = mutableListOf()

            memo.images.forEach {
                mutableList.add(it)
            }
            mutableList.add(outputFile.absolutePath)

            memo.images = mutableList
            val action =
                RemoteUrlFragmentDirections.actionRemoteUrlFragmentToAddEditMemoFragment(memo)
            findNavController().navigate(action)

        } catch (e: IOException) {
            // handle exception
            Toast.makeText(context, R.string.image_url_save_error, Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        private const val TAG = "MemoApp"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.KOREA)
                    .format(System.currentTimeMillis()) + extension
            )
    }
}