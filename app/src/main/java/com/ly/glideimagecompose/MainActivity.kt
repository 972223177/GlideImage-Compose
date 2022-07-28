package com.ly.glideimagecompose

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.ly.glideimagecompose.ui.theme.GlideImageComposeTheme

const val url1 =
    "https://ts4.cn.mm.bing.net/th?id=OIP-C.BqIfd6eSihPzCIW7vlJ8PwHaEo&w=316&h=197&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2"
const val url2 =
    "https://ts4.cn.mm.bing.net/th?id=OIP-C.hJynsX_Y0uUhuQQFb6WNZAHaE8&w=306&h=204&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2"
const val url3 =
    "https://ts3.cn.mm.bing.net/th?id=OIP-C.8olSEek1RZjSQDgPizhxEAHaJ3&w=216&h=288&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlideImageComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.verticalScroll(state = rememberScrollState())
                    ) {
                        GlideImage(
                            model = url1,
                            viewTargetCreator = {
                                object : DrawableImageViewTarget(it) {

                                }
                            },
                            modifier = Modifier
                                .size(100.dp)
                                .background(color = Color.LightGray)
                        ) {
                            centerInside()
                        }
                        View2()
                        GlideImage(
                            model = url3,
                            modifier = Modifier
                                .size(100.dp)
                                .background(color = Color.LightGray)
                        ) {
                            fitCenter()
                        }
                        GlideImage(
                            model = R.mipmap.ic_launcher_round,
                            modifier = Modifier.size(100.dp)
                        ) {
                            centerCrop()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun View2() {
        var view2Switch by remember {
            mutableStateOf(false)
        }
        GlideImage(
            model = if (view2Switch) url2 else url3,
            viewTargetCreator = {
                object : BitmapImageViewTarget(it) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        super.onResourceReady(resource, transition)
                    }
                }
            },
            asBitmap = true,
            modifier = Modifier
                .size(100.dp)
                .background(color = Color.LightGray)
                .clickable {
                    view2Switch = !view2Switch
                }
        ) {
            if (view2Switch) centerCrop() else fitCenter()
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GlideImageComposeTheme {
        Greeting("Android")
    }
}