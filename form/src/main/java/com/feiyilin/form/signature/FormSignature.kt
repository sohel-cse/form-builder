package com.feiyilin.form.signature

import android.net.Uri
import com.feiyilin.form.FormItem

open class FormItemSignature : FormItem() {
    var bitmap: String? = null
    override var canEvaluate = true
    override fun getValueString() = bitmap ?: ""
}

fun <T : FormItemSignature> T.bitmap(bitmap: String) = apply {
    this.bitmap = bitmap
}

