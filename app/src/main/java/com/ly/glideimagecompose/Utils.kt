package com.ly.glideimagecompose

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.assertValidRequest(): Boolean {
    return when (this) {
        is Activity -> !isDestroy()
        is ContextWrapper -> {
            if (baseContext is Activity) {
                !(baseContext as Activity).isDestroy()
            } else {
                true
            }
        }
        else -> true
    }
}

private fun Activity.isDestroy(): Boolean {
    return isFinishing || isDestroyed
}

