package com.feiyilin.form.signature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.feiyilin.form.FormItem
import com.feiyilin.form.FormItemCallback
import com.feiyilin.form.FormViewHolder
import com.feiyilin.form.R

class FormSignatureViewHolder(private val inflater: LayoutInflater, resource: Int, parent: ViewGroup) :
    FormViewHolder(inflater, resource, parent) {
    //    private var imgView: ImageView? = null
    private var signatureStatus: TextView = itemView.findViewById(R.id.signatureStatus)

    companion object {
        const val REQUEST_CODE_ADD_FORM_SIGNATURE = 2323
    }

    override fun bind(s: FormItem, listener: FormItemCallback?) {
        super.bind(s, listener)
        if (s is FormItemSignature) {
            signatureStatus.text = if (s.bitmap.isNullOrEmpty()) "Tap to add signature" else "Tap to view signature"
            signatureStatus.setOnClickListener {

            }
        }
    }
}