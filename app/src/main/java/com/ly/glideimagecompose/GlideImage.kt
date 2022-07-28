package com.ly.glideimagecompose

import android.widget.ImageView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.ImageViewTarget

@Composable
fun GlideImageView(
    model: Any?,
    asBitmap: Boolean = false,
    clearWhenNull: Boolean = false,
    modifier: Modifier = Modifier,
    initImageView: ImageView.() -> Unit = {},
    viewTargetCreator: ((ImageView) -> ImageViewTarget<*>)? = null, // BitmapImageViewTarget or DrawableImageViewTarget
    options: RequestOptions.() -> RequestOptions = { this }
) {
    val state = rememberGlideImageState(model = model).also {
        it.model = model
    }
    GlideImageView(
        state = state,
        options = options,
        asBitmap = asBitmap,
        clearWhenNull = clearWhenNull,
        modifier = modifier,
        viewTargetCreator = viewTargetCreator,
        initImageView = initImageView
    )
}

/**
 * state = rememberGlideImageState(model = R.drawable.xxx or url...)
 */
@Composable
fun GlideImageView(
    state: GlideImageState,
    asBitmap: Boolean = false,
    clearWhenNull: Boolean = false,
    modifier: Modifier = Modifier,
    initImageView: ImageView.() -> Unit = {},
    viewTargetCreator: ((ImageView) -> ImageViewTarget<*>)? = null, // BitmapImageViewTarget or DrawableImageViewTarget
    options: RequestOptions .() -> RequestOptions = { this }
) {
    val context = LocalContext.current
    if (!context.assertValidRequest()) return
    val requestManager = remember {
        Glide.with(context)
    }
    val wrapper = remember {
        GlideWrapper()
    }
    DisposableEffect(key1 = Unit, effect = {
        onDispose {
            wrapper.release()
            requestManager.pauseRequests()
        }
    })

    AndroidView(factory = {
        ImageView(it).apply(initImageView).also { view ->
            wrapper.init(asBitmap, view)
            wrapper.viewTarget = viewTargetCreator?.invoke(view)
        }
    }, modifier = modifier) {
        if (!it.context.assertValidRequest()) return@AndroidView
        val source = state.model
        if (source == null) {
            if (clearWhenNull) {
                it.setImageDrawable(null)
            }
            return@AndroidView
        }
        if (asBitmap) {
            val target = wrapper.viewTarget
            requestManager.asBitmap().load(source).apply(RequestOptions().options())
                .into((target as? BitmapImageViewTarget) ?: wrapper.bitmapViewTarget ?: (object :
                    BitmapImageViewTarget(it) {}))
        } else {
            val target = wrapper.viewTarget
            requestManager.load(source).apply(RequestOptions().options())
                .into(
                    (target as? DrawableImageViewTarget) ?: wrapper.drawableViewTarget ?: (object :
                        DrawableImageViewTarget(it) {})
                )
        }
    }
}

private class GlideWrapper(
    var viewTarget: ImageViewTarget<*>? = null,
    var options: RequestOptions? = null,
    var drawableViewTarget: DrawableImageViewTarget? = null,
    var bitmapViewTarget: BitmapImageViewTarget? = null
) {

    fun init(asBitmap: Boolean, view: ImageView) {
        if (asBitmap) {
            bitmapViewTarget = object : BitmapImageViewTarget(view) {}
        } else {
            drawableViewTarget = object : DrawableImageViewTarget(view) {}
        }
    }

    fun release() {
        viewTarget = null
        options = null
        drawableViewTarget = null
        bitmapViewTarget = null
    }
}


@Composable
fun rememberGlideImageState(model: Any?): GlideImageState =
    remember {
        GlideImageState(model)
    }

class GlideImageState(model: Any?) {
    var model: Any? by mutableStateOf(model)

    fun updateModel(model: Any?) {
        this.model = model
    }
}