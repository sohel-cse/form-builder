package com.nxo.core.utils.permissions

import android.util.Log

const val TAG = "ResultProcessor"

fun resultProcessor(map: Map<String, Boolean?>) =
    map
        .also { Log.i(TAG, "resultProcessor: $map") }
        .all { entry -> entry.value == true }

fun resultProcessor(map: Map<String, Boolean?>, key: String) =
    map
        .also { Log.i(TAG, "resultProcessor: $map") }[key] ?: false
