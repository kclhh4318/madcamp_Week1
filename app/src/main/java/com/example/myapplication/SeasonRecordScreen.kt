package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SeasonRecordScreen(
    season: String,
    savedTexts: MutableList<String>,
    isTextFieldVisible: Boolean,
    onTextFieldVisibilityChanged: (Boolean) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable {
                if (isTextFieldVisible) {
                    onTextFieldVisibilityChanged(false)
                } else {
                    onTextFieldVisibilityChanged(true)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "나의 $season 은", fontSize = 20.sp, modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (savedText in savedTexts) {
                Text(text = savedText, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
            }
        }

        if (isTextFieldVisible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = {
                        if (it.text.length <= 17) {
                            text = it
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.LightGray, shape = RoundedCornerShape(28.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Black),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.Black)
                )
                if (text.text.isEmpty()) {
                    Text(
                        text = "",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterVertically).padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (savedTexts.size < 6 && text.text.isNotBlank()) {
                            savedTexts.add(text.text)
                            text = TextFieldValue("")
                            onTextFieldVisibilityChanged(false)
                        }
                    },
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("기록")
                }
            }
        }
    }
}
