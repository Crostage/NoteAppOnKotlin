package com.bar.youday.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bar.youday.helper.NoteHelper
import java.util.*

@Entity(tableName = "all_notes")
data class Note(
    var title: String = "",
    var text: String = "",
    var type: Int = 0,
    var image: ByteArray? = null,
    var date: String = NoteHelper.convertDate(Date())
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.createByteArray(),
        parcel.readString().toString()
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeInt(type)
        parcel.writeByteArray(image)
        parcel.writeString(date)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (title != other.title) return false
        if (text != other.text) return false
        if (type != other.type) return false
        if (date != other.date) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + type
        result = 31 * result + date.hashCode()
        result = 31 * result + id
        return result
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }


}

