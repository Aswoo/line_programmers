package com.test.memoapp.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.memoapp.R
import kotlinx.android.synthetic.main.bottom_sheet.*

class ActionBottomSheetFragment : BottomSheetDialogFragment() {


    private val args: ActionBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        attach_image_camera.setOnClickListener {
            navigateWithArg("camera")
        }
        attach_image_gallery.setOnClickListener {
            navigateWithArg("gallery")
        }
        attach_image_url.setOnClickListener {
            navigateWithArg("url")
        }
    }

    fun navigateWithArg(selected : String) {


        when(selected){
            "camera" -> ""
            "gallery" -> {
                val action = ActionBottomSheetFragmentDirections.actionActionBottomSheetFragmentToCustomGalleryFragment(args.memoId)
                findNavController().navigate(R.id.action_actionBottomSheetFragment_to_customGalleryFragment)
            }
            "url" -> ""
        }
    }
}