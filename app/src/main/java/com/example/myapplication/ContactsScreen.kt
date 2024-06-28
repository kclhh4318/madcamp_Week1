package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun ContactsScreen(context: Context, seasonMonths: List<Int>, season: String) {
    val contactsLiveData = remember { MutableLiveData<List<Pair<String, String>>>() }
    LaunchedEffect(seasonMonths) {
        val contacts = getTopContacts(context, seasonMonths)
        contactsLiveData.postValue(contacts)
    }

    val contacts by contactsLiveData.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (contacts.isNotEmpty()) {
            contacts.take(3).forEach { contact ->
                Text(
                    text = "${contact.first}: ${contact.second}",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            val message = getRandomMessage(context, season)
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("sms:")
                                putExtra("address", contact.first)
                                putExtra("sms_body", message)
                            }
                            context.startActivity(intent)
                        }
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            Text("연락처가 없습니다.", fontSize = 24.sp, textAlign = TextAlign.Center)
        }
    }
}

fun getTopContacts(context: Context, seasonMonths: List<Int>): List<Pair<String, String>> {
    val resolver = context.contentResolver
    val contactsMap = mutableMapOf<String, Int>()

    val projection = arrayOf(
        CallLog.Calls.NUMBER,
        CallLog.Calls.DATE,
        CallLog.Calls.DURATION
    )

    val cursor = resolver.query(
        CallLog.Calls.CONTENT_URI,
        projection,
        null,
        null,
        null
    )

    cursor?.use {
        val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
        val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
        val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

        if (numberIndex == -1 || dateIndex == -1 || durationIndex == -1) {
            // 컬럼 인덱스가 존재하지 않는 경우를 처리
            return emptyList()
        }

        while (it.moveToNext()) {
            val number = it.getString(numberIndex)
            val date = it.getLong(dateIndex)
            val duration = it.getInt(durationIndex)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            val month = calendar.get(Calendar.MONTH) + 1

            if (seasonMonths.contains(month) && duration > 0) {
                contactsMap[number] = contactsMap.getOrDefault(number, 0) + 1
            }
        }
    }

    val sortedContacts = contactsMap.entries.sortedByDescending { it.value }.take(3)

    return if (sortedContacts.isEmpty()) {
        // 통화 기록이 없는 경우 무작위 연락처를 반환
        getRandomContacts(context, 3)
    } else {
        sortedContacts.map { entry ->
            val name = getContactName(context, entry.key) ?: entry.key
            Pair(name, entry.value.toString())
        }
    }
}

fun getContactName(context: Context, phoneNumber: String): String? {
    val resolver = context.contentResolver
    val uri = Uri.withAppendedPath(
        ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
        Uri.encode(phoneNumber)
    )
    val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
    val cursor = resolver.query(uri, projection, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
            if (nameIndex != -1) {
                return it.getString(nameIndex)
            }
        }
    }
    return null
}
