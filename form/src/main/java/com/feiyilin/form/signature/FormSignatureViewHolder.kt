package com.feiyilin.form.signature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.afollestad.inlineactivityresult.coroutines.startActivityAwaitResult
import com.feiyilin.form.FormItem
import com.feiyilin.form.FormItemCallback
import com.feiyilin.form.FormViewHolder
import com.feiyilin.form.R
import com.feiyilin.form.signature.SignatureActivity.Companion.INTENT_KEY_BITMAP
import kotlinx.coroutines.launch

class FormSignatureViewHolder(private val inflater: LayoutInflater, resource: Int, parent: ViewGroup) :
    FormViewHolder(inflater, resource, parent) {
    //    private var imgView: ImageView? = null
    private var signatureStatus: TextView = itemView.findViewById(R.id.signatureStatus)

    companion object {
        const val REQUEST_CODE_ADD_FORM_SIGNATURE = 2323
    }

    override fun bind(s: FormItem, listener: FormItemCallback?, activity: AppCompatActivity?) {
        super.bind(s, listener, activity)
        if (s is FormItemSignature) {
            signatureStatus.text = if (s.bitmap.isNullOrEmpty()) "Tap to add signature" else "Tap to view signature"
            signatureStatus.setOnClickListener {
                val extras = Bundle()
                extras.putString(INTENT_KEY_BITMAP, s.bitmap)

                activity?.lifecycleScope?.launch {
                    s.bitmap = activity.startActivityAwaitResult<SignatureActivity>(extras).data.getStringExtra("bitmap")
                    signatureStatus.text = if (s.bitmap.isNullOrEmpty()) "Tap to add signature" else "Tap to view signature"
                }

                /* activity?.startActivityForResult<SignatureActivity>(extras) { success, data ->
                     if (success) {
                         toast("Got successful result: $data")
                     }
                 }*/
            }
        }
    }
}