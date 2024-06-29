package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp

val koPubMedium = FontFamily(Font(R.font.kopubmedium))

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 권한 요청
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
                    ButtonScreen(context = this@MainActivity)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 권한이 부여되었을 때 필요한 작업 수행
            } else {
                // 권한이 거부되었을 때 필요한 작업 수행
            }
        }
    }
}

@Composable
fun ButtonScreen(context: MainActivity) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
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
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
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
