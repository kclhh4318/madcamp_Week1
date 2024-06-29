package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

val koPubMedium = FontFamily(Font(R.font.kopubmedium))

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS), 1)
        }

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)  // Faster animation
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = alpha.value),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current

            SeasonButton(
                iconRes = R.drawable.spring,
                text = "봄",
                onClick = { context.startActivity(Intent(context, SpringActivity::class.java)) },
                iconSize = 100.dp
            )
            SeasonButton(
                iconRes = R.drawable.summer,
                text = "여름",
                onClick = { context.startActivity(Intent(context, SummerActivity::class.java)) },
                iconSize = 100.dp
            )
            SeasonButton(
                iconRes = R.drawable.autumn,
                text = "가을",
                onClick = { context.startActivity(Intent(context, AutumnActivity::class.java)) },
                iconSize = 100.dp
            )
            SeasonButton(
                iconRes = R.drawable.winter,
                text = "겨울",
                onClick = { context.startActivity(Intent(context, WinterActivity::class.java)) },
                iconSize = 100.dp
            )
        }
    }
}

@Composable
fun SeasonButton(iconRes: Int, text: String, onClick: () -> Unit, iconSize: Dp, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeResource(context.resources, iconRes, options)
    options.inSampleSize = calculateInSampleSize(options, with(density) { iconSize.roundToPx() }, with(density) { iconSize.roundToPx() })
    options.inJustDecodeBounds = false
    val bitmap = BitmapFactory.decodeResource(context.resources, iconRes, options)
    val imageBitmap = bitmap.asImageBitmap()

    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = text,
            modifier = Modifier.padding(top = 8.dp),
            fontFamily = koPubMedium
        )
    }
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}
