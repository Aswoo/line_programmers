package com.test.memoapp.ui.addeditmemo

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.test.memoapp.R
import com.test.memoapp.databinding.AddmemoFragBinding
import com.test.memoapp.di.Injectable
import com.test.memoapp.util.setupSnackbar
import javax.inject.Inject

class AddEditMemoFragment : Fragment(),Injectable{

    private lateinit var viewDataBinding: AddmemoFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AddEditMemoViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.addmemo_frag, container, false)
        viewDataBinding = AddmemoFragBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()
        /*
        this.setupRefreshLayout(viewDataBinding.refreshLayout)
        viewModel.start(args.taskId)

         */
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarMessage, Snackbar.LENGTH_SHORT)
    }

    fun imagePick(){
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    // something is wrong
                }

                val clipData = data?.clipData
                if (clipData != null) { // handle multiple photo
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri

                        viewModel.addImagePath(uri)
                    }
                } else { // handle single photo
                    val uri = data?.data
                    viewModel.addImagePath(uri)
                }
            }
        }
    }

    companion object {
        private const val REQUEST_PICK_PHOTO = 1
    }


}