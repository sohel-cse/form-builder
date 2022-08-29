package com.feiyilin.form.file

import android.net.Uri
import com.feiyilin.form.FormItem

open class FormItemFile : FormItem() {
    var galleryOnly: Boolean = false
    var multiple: Boolean = true
    var fileUris: MutableList<Uri> = mutableListOf()
    var onCameraClicked: ((String) -> Unit)? = null
    var onGalleryClicked: ((String) -> Unit)? = null
    var onDetailClicked: ((String) -> Unit)? = null
    override var canEvaluate = true
    override fun getValueString() = fileUris.joinToString { it.toString() }
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

