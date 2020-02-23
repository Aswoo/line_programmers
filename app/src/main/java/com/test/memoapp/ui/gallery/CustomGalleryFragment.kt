package com.test.memoapp.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.memoapp.R
import com.test.memoapp.di.Injectable
import com.test.memoapp.ui.gallery.adapter.GalleryPicturesAdapter
import com.test.memoapp.ui.gallery.model.GalleryPicture
import com.test.memoapp.ui.gallery.model.GalleryPictures
import com.test.memoapp.ui.gallery.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.activity_multi_gallery_ui.*
import kotlinx.android.synthetic.main.memodetail_item.*
import kotlinx.android.synthetic.main.toolbar.*
import mobin.customgallery.multipicker.ui.gallery.adapter.SpaceItemDecoration


class CustomGalleryFragment : Fragment() {


    private lateinit var adapter: GalleryPicturesAdapter
    private lateinit var galleryViewModel: GalleryViewModel

    private val args: CustomGalleryFragmentArgs by navArgs()

    private lateinit var pictures: ArrayList<GalleryPicture>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_multi_gallery_ui,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestReadStoragePermission()
    }

    private fun requestReadStoragePermission() {
        val readStorage = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    readStorage
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(readStorage), 3)
        } else init()
    }

    private fun init() {
        galleryViewModel = ViewModelProviders.of(this)[GalleryViewModel::class.java]
        updateToolbar(0)
        val layoutManager = GridLayoutManager(context, 3)
        rv.layoutManager = layoutManager
        rv.addItemDecoration(SpaceItemDecoration(8))
        pictures = ArrayList(galleryViewModel.getGallerySize(context))
        adapter = GalleryPicturesAdapter(pictures, 10)
        rv.adapter = adapter


        adapter.setAfterSelectionListener {
            updateToolbar(getSelectedItemsCount())
        }

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findLastVisibleItemPosition() == pictures.lastIndex) {
                    loadPictures(25)
                }
            }
        })

        tvDone.setOnClickListener {
            //super.onBackPressed()

            val images = adapter.getSelectedItems().toTypedArray<GalleryPicture>()

            val action = CustomGalleryFragmentDirections.actionCustomGalleryFragmentToAddEditMemoFragment(images,args.memoId)

            findNavController().navigate(action)

        }

        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        loadPictures(25)

    }


    private fun getSelectedItemsCount() = adapter.getSelectedItems().size


    private fun loadPictures(pageSize: Int) {
        context?.let {
            galleryViewModel.getImagesFromGallery(it, pageSize) {
                if (it.isNotEmpty()) {
                    pictures.addAll(it)
                    adapter.notifyItemRangeInserted(pictures.size, it.size)
                }
                Log.i("GalleryListSize", "${pictures.size}")

            }
        }

    }

    private fun updateToolbar(selectedItems: Int) {
        val data = if (selectedItems == 0) {
            tvDone.visibility = View.GONE
            getString(R.string.txt_gallery)
        } else {
            tvDone.visibility = View.VISIBLE
            "$selectedItems"
        }
        tvTitle.text = data
    }

    /*
    override fun onBackPressed() {
        if (adapter.removedSelection()) {
            updateToolbar(0)
        } else {
            super.onBackPressed()
        }


    }

    private fun showToast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            init()
        else {
            showToast("Permission Required to Fetch Gallery.")
            super.onBackPressed()
        }
    }

     */

}
