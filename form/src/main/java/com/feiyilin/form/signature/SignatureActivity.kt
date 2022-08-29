package com.feiyilin.form.signature

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.feiyilin.form.databinding.ActivityFormSignatureBinding
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SignatureActivity : AppCompatActivity(), SignaturePad.OnSignedListener, SignatureActivityListener {
    companion object {
        const val TAG = "SignatureActivity"
        const val INTENT_KEY_BITMAP = "INTENT_KEY_BITMAP"
    }

    private lateinit var binding: ActivityFormSignatureBinding
    private var bitmap: String? = null

    private fun setupActionBar(toolbar: Toolbar, upEnabled: Boolean) {
        setSupportActionBar(toolbar)
        if (upEnabled) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            toolbar.setNavigationOnClickListener { onBackPressed() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityFormSignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar(binding.toolbar, true)
        supportActionBar!!.title = "Signature"

        if (intent != null) {
            bitmap = intent.getStringExtra(INTENT_KEY_BITMAP)
        }

        try {
            val cleanImage: String = bitmap!!.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "")
            val decodedString = Base64.decode(cleanImage, Base64.DEFAULT)
            val decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            if (decodedBitmap != null) binding.signaturePad.signatureBitmap = decodedBitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.signaturePad.setOnSignedListener(this)
        binding.btnCancel.setOnClickListener { onSignatureClear() }
        binding.btnDone.setOnClickListener { onSignatureDone() }
    }


    override fun onStartSigning() {
        Log.e(TAG, "onStartSigning: ")
    }

    override fun onSigned() {
        Log.e(TAG, "onSigned: ")
    }

    override fun onClear() {
        Log.e(TAG, "onClear: ")
    }

    override fun onSignatureClear() {
        binding.signaturePad.clear()
    }

    override fun onSignatureDone() {
        val bitmap: Bitmap = binding.signaturePad.signatureBitmap
        if (bitmap != null) {
            Log.e(TAG, "onSignatureDone: bitmap")
            //            saveSignature(bitmap);
            convertToBase64(bitmap)
        }
    }

    /**
     * reduces the size of the image
     *
     * @param image
     * @param maxSize
     * @return
     */
    fun getResizedBitmap(image: Bitmap): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapWidthRatio = width.toFloat() / 500f
        if (bitmapWidthRatio > 1) {
            width = 500
            height = (image.height / bitmapWidthRatio).toInt()
        } else {
            height = image.height
            width = image.width
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun convertToBase64(bitmap: Bitmap) {
//        dataBinding.setStatus(Status.LOADING)
        lifecycleScope.launch {
            val encoded = withContext(Dispatchers.IO) {
                val bitmap1 = getResizedBitmap(bitmap)
                val bos = ByteArrayOutputStream()
                bitmap1.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
                val bitmapdata = bos.toByteArray()
                Log.e(TAG, "doInBackground: byte size: " + bitmapdata.size)
                var encoded = Base64.encodeToString(bitmapdata, Base64.DEFAULT)
                if (encoded.indexOf("data:") < 0) {
                    encoded = "data:image/png;base64,$encoded"
                }
                encoded
            }
            Log.e(TAG, "onPostExecute:base64:  $encoded")
            setResult(RESULT_OK, Intent().putExtra("bitmap", encoded))
            finish()
        }
    }

    private fun saveSignature(bitmap: Bitmap) {
//        dataBinding.setStatus(Status.LOADING)
        lifecycleScope.launch {
            val path = withContext(Dispatchers.IO) {
                val bitmap1 = bitmap
                val f = File(filesDir, "signature.png")
                try {
                    f.createNewFile()
                    val bos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
                    val bitmapdata = bos.toByteArray()
                    val fos = FileOutputStream(f)
                    fos.write(bitmapdata)
                    fos.flush()
                    fos.close()
                    f.absolutePath
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
            }
            if (!TextUtils.isEmpty(path)) {
                setResult(RESULT_OK, Intent().putExtra("path", path))
                finish()
            }
        }
    }
}