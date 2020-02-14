package com.test.memoapp.data

import java.util.*

data class Memo(var title : String,
                var description : String,
                var image : ByteArray,
               var id: String = UUID.randomUUID().toString()) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Memo

        if (title != other.title) return false
        if (description != other.description) return false
        if (!image.contentEquals(other.image)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}