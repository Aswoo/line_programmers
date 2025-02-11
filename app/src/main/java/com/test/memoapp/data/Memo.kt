package com.test.memoapp.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import java.util.*


@Entity(tableName = "memos")
@TypeConverters(MemoTypeConverters::class)
@Parcelize
data class Memo @JvmOverloads constructor(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "images") var images : List<String> = emptyList(),
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
) : Parcelable {

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}