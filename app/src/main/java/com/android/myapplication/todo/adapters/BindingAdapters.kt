package com.android.myapplication.todo.adapters

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.todo.R
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.data.Reminders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import java.io.File

@BindingAdapter("notesList")
fun RecyclerView.submitList(notes: List<Notes>?) {
    val adapter = this.adapter as NotesListAdapter
    adapter.submitList(notes)
}

@BindingAdapter("remindersList")
fun RecyclerView.submitReminderList(reminders: List<Reminders>?) {
    val adapter = this.adapter as RemindersListAdapter
    adapter.submitList(reminders)
}

@BindingAdapter("placeHolder")
fun ImageView.placeholderImage(drawable: Drawable?) {
    Glide.with(this.context).load(drawable).into(this)
}

@BindingAdapter("displayIsFavorite")
fun ImageView.displayImage(isFavorite: Boolean?) {
    isFavorite?.let {
        if (it) {
            setImageDrawable(this.context.getDrawable(R.drawable.ic_favorite_active))
        } else {
            setImageDrawable(this.context.getDrawable(R.drawable.ic_favorite_inactive))
        }
    }
}

@BindingAdapter("resolveIntent")
fun FloatingActionButton.resolveIntent(intent: Intent) {
    when (intent.resolveActivity(this.context.packageManager)) {
        null -> {
            setImageResource(R.drawable.ic_no_photo)
            isEnabled = false
        }
        else -> {
            setImageResource(R.drawable.ic_camera)
            isEnabled = true
        }


    }
}

@BindingAdapter("noteImage")
fun ImageView.setNoteImage(imageFile: File?) {
    Log.d("GLIDE", "setNoteImage: ${imageFile} ")
  imageFile?.let {
      if(it.exists()){
          visibility = View.VISIBLE
          Picasso.get()
              .load(imageFile)
              .placeholder(R.drawable.loading_animation)
              .memoryPolicy(MemoryPolicy.NO_CACHE)
              .into(this)
      }else{
          visibility = View.GONE
      }
  }
}

