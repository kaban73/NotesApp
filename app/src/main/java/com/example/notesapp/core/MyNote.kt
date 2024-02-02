package com.example.notesapp.core

import java.text.SimpleDateFormat
import java.util.Date

data class MyNote(
    val id : Long,
    val title : String,
    val text : String,
    val lastDate : String = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        .format(Date())
)