package com.example.myapplication

import android.content.Context
import android.provider.CallLog
import android.provider.ContactsContract
import java.util.*
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.random.Random

data class SongInfo(
    val title: String,
    val artist: String
)

// 계절별 음악 리스트
val seasonMusic = mapOf(
    "봄" to listOf(
        Pair("sprs1", "spr1"),
        Pair("sprs2", "spr2"),
        Pair("sprs3", "spr3"),
        Pair("sprs4", "spr4"),
        Pair("sprs5", "spr5"),
        Pair("sprs6", "spr6")
    ),
    "여름" to listOf(
        Pair("sms1", "sum1"),
        Pair("sms2", "sum2"),
        Pair("sms3", "sum3"),
        Pair("sms4", "sum4"),
        Pair("sms5", "sum5"),
        Pair("sms6", "sum6")
    ),
    "가을" to listOf(
        Pair("ats1", "aut1"),
        Pair("ats2", "aut2"),
        Pair("ats3", "aut3"),
        Pair("ats4", "aut4"),
        Pair("ats5", "aut5"),
        Pair("ats6", "aut6")
    ),
    "겨울" to listOf(
        Pair("winter_music_1", "win1"),
        Pair("winter_music_2", "win2"),
        Pair("winter_music_3", "win3"),
        Pair("winter_music_4", "win4"),
        Pair("winter_music_5", "win5"),
        Pair("winter_music_6", "win6")
    )
)

val songInfos = mapOf(
    "sprs1" to SongInfo("Spring Song 1", "Artist A"),
    "sprs2" to SongInfo("Spring Song 2", "Artist B"),
    "sprs3" to SongInfo("Spring Song 3", "Artist C"),
    "sprs_4" to SongInfo("Spring Song 4", "Artist D"),
    "sprs5" to SongInfo("Spring Song 5", "Artist E"),
    "sprs6" to SongInfo("Spring Song 6", "Artist F"),
    "summer_music_1" to SongInfo("Summer Song 1", "Artist G"),
    "summer_music_2" to SongInfo("Summer Song 2", "Artist G"),
    "summer_music_3" to SongInfo("Summer Song 3", "Artist G"),
    "summer_music_4" to SongInfo("Summer Song 4", "Artist G"),
    "summer_music_5" to SongInfo("Summer Song 5", "Artist G", 2020),
    "summer_music_6" to SongInfo("Summer Song 6", "Artist G", 2020),
    "ats1" to SongInfo("Autumn Song 1", "Artist H", 2020),
    "ats2" to SongInfo("Autumn Song 2", "Artist H", 2020),
    "ats3" to SongInfo("Autumn Song 3", "Artist H", 2020),
    "ats4" to SongInfo("Autumn Song 4", "Artist H", 2020),
    "ats5" to SongInfo("Autumn Song 5", "Artist H", 2020),
    "autumn_music_6" to SongInfo("Autumn Song 6", "Artist H", 2020),
    "winter_music_1" to SongInfo("Winter Song 1", "Artist I", 2020),
    "winter_music_2" to SongInfo("Winter Song 2", "Artist I", 2020),
    "winter_music_3" to SongInfo("Winter Song 3", "Artist I", 2020),
    "winter_music_4" to SongInfo("Winter Song 4", "Artist I", 2020),
    "winter_music_5" to SongInfo("Winter Song 5", "Artist I", 2020),
    "winter_music_6" to SongInfo("Winter Song 6", "Artist I", 2020),
)

fun getRandomContacts(context: Context, count: Int): List<Pair<String, String>> {
    val contacts = mutableListOf<Pair<String, String>>()
    val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER)

    val cursor = context.contentResolver.query(uri, projection, null, null, "RANDOM() LIMIT $count")
    cursor?.use {
        while (it.moveToNext()) {
            val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            contacts.add(name to number)
        }
    }
    return contacts
}

fun getRandomMessage(context: Context, season: String): String {
    val textFiles = when (season) {
        "봄" -> listOf("spp1.txt", "spp2.txt", "spp3.txt", "spp4.txt", "spp5.txt", "spp6.txt")
        "여름" -> listOf("sup1.txt", "sup2.txt", "sup3.txt", "sup4.txt", "sup5.txt", "sup6.txt")
        "가을" -> listOf("ap1.txt", "ap2.txt", "ap3.txt", "ap4.txt", "ap5.txt", "ap6.txt")
        "겨울" -> listOf("wp1.txt", "wp2.txt", "wp3.txt", "wp4.txt", "wp5.txt", "wp6.txt", "wp7.txt")
        else -> listOf()
    }
    val selectedFile = textFiles[Random.nextInt(textFiles.size)]
    val inputStream = context.assets.open(selectedFile)
    return BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
}