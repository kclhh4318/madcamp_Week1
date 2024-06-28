package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.compose.runtime.livedata.observeAsState

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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (contacts.isNotEmpty()) {
            for (contact in contacts) {
                Text(
                    "${contact.first}: ${contact.second}",
                    modifier = Modifier.clickable {
                        val intent = Intent(context, ContactDetailActivity::class.java).apply {
                            putExtra("contactName", contact.first)
                            putExtra("season", season)
                        }
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            Text("연락처가 없습니다.")
        }
    }
}
