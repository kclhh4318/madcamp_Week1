package com.example.myapplication

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

// ContactsScreen을 임포트합니다.
import com.example.myapplication.ContactsScreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun VerticalPagerExample(context: Context, season: String) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val seasonMonths = when (season) {
        "봄" -> listOf(3, 4, 5)
        "여름" -> listOf(6, 7, 8)
        "가을" -> listOf(9, 10, 11)
        "겨울" -> listOf(12, 1, 2)
        else -> listOf(3, 4, 5) // 기본값을 봄으로 설정
    }

    val savedTexts = remember { mutableStateListOf<String>() }
    var isTextFieldVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        VerticalPager(
            state = pagerState,
            count = 3,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> ContactsScreen(context = context, seasonMonths = seasonMonths, season = season)
                1 -> SeasonMusicScreen(context = context, season = season)
                2 -> SeasonRecordScreen(
                    season = season,
                    savedTexts = savedTexts,
                    isTextFieldVisible = isTextFieldVisible,
                    onTextFieldVisibilityChanged = { isTextFieldVisible = it }
                )
            }
        }
    }
}
