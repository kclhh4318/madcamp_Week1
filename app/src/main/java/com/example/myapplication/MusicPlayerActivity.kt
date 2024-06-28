package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState


class MusicPlayerActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val musicTitle = intent.getStringExtra("musicTitle") ?: "No Music Selected"
        val imageName = intent.getStringExtra("imageName") ?: ""

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicPlayerScreen(
                        musicTitle = musicTitle,
                        imageName = imageName,
                        onPlayClick = { playMusic(musicTitle) },
                        onStopClick = { stopMusic() },
                        onCloseClick = { finish() },
                        onShareClick = { shareMusic(musicTitle) }
                    )
                }
            }
        }
    }

    private fun playMusic(title: String) {
        stopMusic() // 기존 재생 중인 음악이 있으면 중지

        val resId = resources.getIdentifier(title.toLowerCase().replace(" ", "_"), "raw", packageName)
        Log.d("ResourceID", "Music resId: $resId for title: $title")
        if (resId != 0) {
            mediaPlayer = MediaPlayer.create(this, resId)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                finish() // 음악 재생이 완료되면 Activity 종료
            }
        } else {
            // 리소스를 찾을 수 없을 때 처리
            println("Resource not found for title: $title")
        }
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun shareMusic(title: String) {
        val resId = resources.getIdentifier(title.toLowerCase().replace(" ", "_"), "raw", packageName)
        Log.d("ResourceID", "Share Music resId: $resId for title: $title")
        if (resId != 0) {
            val uri = Uri.parse("android.resource://$packageName/$resId")
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "audio/mp3"
            }
            startActivity(Intent.createChooser(shareIntent, "Share music using"))
        } else {
            // 리소스를 찾을 수 없을 때 처리
            println("Resource not found for title: $title")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
    }
}

@Composable
fun MusicPlayerScreen(musicTitle: String, imageName: String, onPlayClick: () -> Unit, onStopClick: () -> Unit, onCloseClick: () -> Unit, onShareClick: () -> Unit) {
    val context = LocalContext.current
    var showDetails by remember { mutableStateOf(false) }
    var songInfo by remember { mutableStateOf(SongInfo("", "")) }
    var imageResId by remember { mutableStateOf(0) }

    LaunchedEffect(musicTitle) {
        withContext(Dispatchers.IO) {
            songInfo = songInfos[musicTitle] ?: SongInfo("Unknown Title", "Unknown Artist")
        }
        imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        Log.d("ResourceID", "Image resId: $imageResId for imageName: $imageName")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -50) {
                        showDetails = true
                    } else if (dragAmount > 50) {
                        showDetails = false
                    }
                }
            }
    ) {
        if (showDetails) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(56.dp))
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                } else {
                    Text("Image not found", color = Color.Red)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Artist: ${songInfo.artist}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Title: ${songInfo.title}", fontSize = 16.sp)
            }
        } else {
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.Center)
                        .clickable { onPlayClick() }
                )
            } else {
                Text("Image not found", color = Color.Red, modifier = Modifier.align(Alignment.Center))
            }
        }

        IconButton(
            onClick = { onShareClick() },
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = context.resources.getIdentifier("share", "drawable", context.packageName)),
                contentDescription = "Share",
                tint = Color.Black
            )
        }

        Icon(
            painter = painterResource(id = context.resources.getIdentifier("exit", "drawable", context.packageName)),
            contentDescription = "Close",
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .clickable { onCloseClick() }
        )
    }
}
