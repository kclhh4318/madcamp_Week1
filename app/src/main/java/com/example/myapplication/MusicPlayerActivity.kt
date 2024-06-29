package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.clickable

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
                        onCloseClick = { finish() },
                        onShareClick = { shareMusic(musicTitle) }
                    )
                }
            }
        }
    }

    private fun playMusic(title: String) {
        stopMusic() // 기존 재생 중인 음악이 있으면 중지

        try {
            val resId = resources.getIdentifier(title.toLowerCase().replace(" ", "_"), "raw", packageName)
            Log.d("ResourceID", "Music resId: $resId for title: $title")
            if (resId != 0) {
                Log.d("MusicPlayerActivity", "Creating MediaPlayer for resId: $resId")
                mediaPlayer = MediaPlayer.create(this, resId)
                mediaPlayer?.setOnCompletionListener {
                    Log.d("MusicPlayerActivity", "Music completed")
                    finish() // 음악 재생이 완료되면 Activity 종료
                }
                mediaPlayer?.setOnErrorListener { mp, what, extra ->
                    Log.e("MusicPlayerActivity", "MediaPlayer error: what=$what, extra=$extra")
                    true // true를 반환하여 에러 처리를 완료했음을 나타냅니다.
                }
                if (mediaPlayer != null) {
                    Log.d("MusicPlayerActivity", "Starting MediaPlayer")
                    mediaPlayer?.start()
                } else {
                    Log.e("MusicPlayerActivity", "MediaPlayer creation failed")
                }
            } else {
                // 리소스를 찾을 수 없을 때 처리
                Log.e("MusicPlayerActivity", "Resource not found for title: $title")
            }
        } catch (e: Exception) {
            Log.e("MusicPlayerActivity", "Exception while creating MediaPlayer", e)
        }
    }

    private fun stopMusic() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: IllegalStateException) {
            Log.e("MusicPlayerActivity", "Error stopping MediaPlayer", e)
        } finally {
            mediaPlayer = null
            Log.d("MusicPlayerActivity", "MediaPlayer stopped and released")
        }
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
fun MusicPlayerScreen(
    musicTitle: String,
    imageName: String,
    onPlayClick: () -> Unit,
    onCloseClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val context = LocalContext.current
    var imageResId by remember { mutableStateOf(0) }
    var songInfo by remember { mutableStateOf(SongInfo("Unknown Title", "Unknown Artist")) }

    LaunchedEffect(musicTitle) {
        imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        songInfo = songInfos[musicTitle] ?: SongInfo("Unknown Title", "Unknown Artist")
        Log.d("ResourceID", "Image resId: $imageResId for imageName: $imageName")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(150.dp)) //앨범 위 여백
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    if (imageResId != 0) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(300.dp)
                        )
                    } else {
                        Text("Image not found", color = Color.Red)
                    }
                }
                Spacer(modifier = Modifier.height(280.dp)) //앨범과 텍스트 사이 여백

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Title: ${songInfo.title}", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Artist: ${songInfo.artist}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(270.dp)) //텍스트 하단 여백
            }
        }

        IconButton(
            onClick = { onShareClick() },
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(id = context.resources.getIdentifier("share", "drawable", context.packageName)),
                contentDescription = "Share",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
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
