package com.example.notesapp.core

interface Now {
    fun timeInMillis() : Long
    class Base : Now {
        override fun timeInMillis(): Long =
            System.currentTimeMillis()
    }
}