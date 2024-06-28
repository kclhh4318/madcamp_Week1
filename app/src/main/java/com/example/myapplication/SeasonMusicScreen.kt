package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SeasonMusicScreen(context: Context, season: String) {
    val musicList = seasonMusic[season] ?: emptyList()

    if (musicList.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(musicList.size) { index ->
                val (music, image) = musicList[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(context, MusicPlayerActivity::class.java).apply {
                                putExtra("musicTitle", music)
                                putExtra("imageName", image)
                            }
                            context.startActivity(intent)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val imageResId = context.resources.getIdentifier(image, "drawable", context.packageName)
                    Log.d("ResourceID", "Grid Image resId: $imageResId for imageName: $image")
                    if (imageResId != 0) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = music,
                            modifier = Modifier.size(150.dp)
                        )
                    } else {
                        Text("Image not found", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = music)
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("음악 리스트가 없습니다.")
        }
    }
}
