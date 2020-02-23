package com.test.memoapp.ui.gallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.memoapp.R
import com.test.memoapp.ui.gallery.model.GalleryPicture
import kotlinx.android.synthetic.main.multi_gallery_listitem.view.*


class GalleryPicturesAdapter(private val list: List<GalleryPicture>) : RecyclerView.Adapter<GVH>() {

    init {
        initSelectedIndexList()
    }

    constructor(list: List<GalleryPicture>, selectionLimit: Int) : this(list) {
        setSelectionLimit(selectionLimit)
    }

    private lateinit var onClick: (GalleryPicture) -> Unit
    private lateinit var afterSelectionCompleted: () -> Unit
    private lateinit var selectedIndexList: ArrayList<Int> // only limited items are selectable.


    private fun initSelectedIndexList() {
        selectedIndexList = ArrayList()
    }

    fun setSelectionLimit(selectionLimit: Int) {
        removedSelection()
        initSelectedIndexList()
    }

    fun setOnClickListener(onClick: (GalleryPicture) -> Unit) {
        this.onClick = onClick
    }

    fun setAfterSelectionListener(afterSelectionCompleted: () -> Unit) {
        this.afterSelectionCompleted = afterSelectionCompleted
    }

    private fun checkSelection(position: Int) {

        if (getItem(position).isSelected)
            selectedIndexList.add(position)
        else {
            selectedIndexList.remove(position)
        }
    }

    //    Useful Methods to provide delete feature.

//    fun deletePicture(picture: GalleryPicture) {
//        deletePicture(list.indexOf(picture))
//    }
//
//    fun deletePicture(position: Int) {
//        if (File(getItem(position).path).delete()) {
//            list.removeAt(position)
//            notifyItemRemoved(position)
//        } else {
//            Log.e("GalleryPicturesAdapter", "Deletion Failed")
//        }
//    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GVH {
        val vh =
            GVH(LayoutInflater.from(p0.context).inflate(R.layout.multi_gallery_listitem, p0, false))
        vh.containerView.setOnClickListener {


            val position = vh.adapterPosition
            val picture = getItem(position)

            handleSelection(position, it.context)
            notifyItemChanged(position)
            checkSelection(position)
            afterSelectionCompleted()

        }
        return vh
    }

    private fun handleSelection(position: Int, context: Context) {

        val picture = getItem(position)

        picture.isSelected = (picture.isSelected).not()

    }

    private fun getItem(position: Int) = list[position]

    override fun onBindViewHolder(p0: GVH, p1: Int) {
        val picture = list[p1]
        Glide.with(p0.containerView).load(picture.path).into(p0.containerView.ivImg)


        if (picture.isSelected) {
            p0.containerView.vSelected.visibility = View.VISIBLE
        } else {
            p0.containerView.vSelected.visibility = View.GONE
        }
    }

    override fun getItemCount() = list.size


    fun getSelectedItems() = selectedIndexList.map {
        list[it]
    }


    fun removedSelection(){

        selectedIndexList.forEach {
            list[it].isSelected = false
        }
        selectedIndexList.clear()
        notifyDataSetChanged()
    }
}