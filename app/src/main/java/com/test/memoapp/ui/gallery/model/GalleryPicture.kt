package com.test.memoapp.ui.gallery.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GalleryPicture(val path: String) : Parcelable {
    var isSelected = false
}