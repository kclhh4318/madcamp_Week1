package com.example.myapplication

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.platform.LocalContext

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
                        onCloseClick = { finish() }
                    )
                }
            }
        }
    }

    private fun playMusic(title: String) {
        stopMusic() // 기존 재생 중인 음악이 있으면 중지

        val resId = resources.getIdentifier(title.toLowerCase().replace(" ", "_"), "raw", packageName)
        if (resId != 0) {
            mediaPlayer = MediaPlayer.create(this, resId)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                finish() // 음악 재생이 완료되면 Activity 종료
            }
        }
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
    }
}

@Composable
fun MusicPlayerScreen(musicTitle: String, imageName: String, onPlayClick: () -> Unit, onStopClick: () -> Unit, onCloseClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = LocalContext.current.resources.getIdentifier("ic_close", "drawable", LocalContext.current.packageName)),
            contentDescription = "Close",
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .clickable { onCloseClick() }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp), // 상단 패딩을 추가하여 아이콘과 텍스트 사이에 공간을 확보
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = musicTitle, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(
                    id = LocalContext.current.resources.getIdentifier(imageName, "drawable", LocalContext.current.packageName)
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clickable { onPlayClick() }
            )
        }
    }
}
