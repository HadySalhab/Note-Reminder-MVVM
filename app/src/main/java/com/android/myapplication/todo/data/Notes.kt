package com.android.myapplication.todo.data

import android.accounts.AuthenticatorDescription
import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val _id:Int = 0,
    @ColumnInfo(name="note_identifier")  val noteIdentifier:String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @SuppressLint("SimpleDateFormat") @ColumnInfo(name = "date") var date:String=SimpleDateFormat("EEE, d MMM yyyy").format(Date()),
    @ColumnInfo(name = "favorite") var isFavorite:Boolean = false
){
    val titleForNoteList:String
    get() = if(title.isNotEmpty()) title else description

    val isEmpty
    get() = title.isEmpty() || description.isEmpty()

    val photoFileName
    get() = "IMG_$noteIdentifier.jpg"

}