package com.feiyilin.form.file

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.feiyilin.form.FormItem
import com.feiyilin.form.FormItemCallback
import com.feiyilin.form.FormViewHolder
import com.feiyilin.form.R

class FormFileViewHolder(inflater: LayoutInflater, resource: Int, parent: ViewGroup) :
    FormViewHolder(inflater, resource, parent) {
    //    private var imgView: ImageView? = null
    private var fileCount: TextView = itemView.findViewById(R.id.fileCount)
    private var openCamera: LinearLayout = itemView.findViewById(R.id.btnOpenCamera)
    private var openGallery: LinearLayout = itemView.findViewById(R.id.btnOpenGallery)

    override fun bind(s: FormItem, listener: FormItemCallback?, activity: AppCompatActivity?) {
        super.bind(s, listener, activity)
        if (s is FormItemFile) {
            fileCount.text = "${s.fileUris.size} file(s) selected"
            openCamera.setOnClickListener { s.onCameraClicked?.invoke(s.tag) }
            openGallery.setOnClickListener { s.onGalleryClicked?.invoke(s.tag) }
            openCamera.visibility = if (s.galleryOnly) View.GONE else View.VISIBLE
        }
    }
}