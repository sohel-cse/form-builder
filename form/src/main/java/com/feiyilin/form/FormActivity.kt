package com.feiyilin.form

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class FormActivity : AppCompatActivity() {
    val TAG = "FormActivity"
    var recyclerView: RecyclerView? = null
    val adapter: FormRecyclerAdapter?
        get() {
            return recyclerView?.adapter as? FormRecyclerAdapter
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_activity)

        recyclerView = findViewById<RecyclerView>(R.id.formRecyclerView)
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(this@FormActivity)

            adapter = FormRecyclerAdapter(onFormItemListener).apply {
            }
        }
        initForm()

        findViewById<Button>(R.id.submit).setOnClickListener {
            Log.i(TAG, "onSubmit: ${adapter?.validateAllItems()?.joinToString { "${it.first}: ${it.second}" }}")
            Log.i(TAG, "onSubmit: ${adapter?.getAllItems()?.map { Pair(it.tag, it.getValueString()) }?.joinToString { "${it.first}: ${it.second}" }}")
        }
    }

    open var onFormItemListener: FormItemCallback? = null

    abstract fun initForm()

    fun drawable(res: Int): Drawable? {
        return ContextCompat.getDrawable(this, res)
    }

    fun color(res: Int): Int {
        return ContextCompat.getColor(this, res)
    }
}