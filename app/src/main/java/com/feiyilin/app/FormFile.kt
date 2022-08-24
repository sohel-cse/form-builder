package com.feiyilin.app

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.feiyilin.form.FormItem
import com.feiyilin.form.FormItemCallback
import com.feiyilin.form.FormViewHolder
import com.feiyilin.form.R

open class FormItemFile : FormItem() {
    var galleryOnly: Boolean = false
    var multiple: Boolean = true
    var fileUris: MutableList<Uri> = mutableListOf()
    var onCameraClicked: ((String) -> Unit)? = null
    var onGalleryClicked: ((String) -> Unit)? = null
    var onDetailClicked: ((String) -> Unit)? = null
}

fun <T : FormItemFile> T.multiple(multiple: Boolean) = apply {
    this.multiple = multiple
}

fun <T : FormItemFile> T.galleryOnly(galleryOnly: Boolean) = apply {
    this.galleryOnly = galleryOnly
}

fun <T : FormItemFile> T.fileUris(fileUris: List<Uri>) = apply {
    this.fileUris = fileUris.toMutableList()
}

fun <T : FormItemFile> T.onCameraClicked(onCameraClicked: ((String) -> Unit)?) = apply {
    this.onCameraClicked = onCameraClicked
}

fun <T : FormItemFile> T.onGalleryClicked(onGalleryClicked: ((String) -> Unit)?) = apply {
    this.onGalleryClicked = onGalleryClicked
}

fun <T : FormItemFile> T.onDetailClicked(onDetailClicked: ((String) -> Unit)?) = apply {
    this.onDetailClicked = onDetailClicked
}

class FormFileViewHolder(inflater: LayoutInflater, resource: Int, parent: ViewGroup) :
    FormViewHolder(inflater, resource, parent) {
    //    private var imgView: ImageView? = null
    private var fileCount: TextView = itemView.findViewById(R.id.fileCount)
    private var openCamera: LinearLayout = itemView.findViewById(R.id.btnOpenCamera)
    private var openGallery: LinearLayout = itemView.findViewById(R.id.btnOpenGallery)

    override fun bind(s: FormItem, listener: FormItemCallback?) {
        super.bind(s, listener)
        if (s is FormItemFile) {
            fileCount.text = "${s.fileUris.size} file(s) selected"
            openCamera.setOnClickListener { s.onCameraClicked?.invoke(s.tag) }
            openGallery.setOnClickListener { s.onGalleryClicked?.invoke(s.tag) }
            openCamera.visibility = if (s.galleryOnly) View.GONE else View.VISIBLE
        }
    }
}