package com.example.notesapp.create

import com.example.notesapp.core.LiveDataWrapper

interface CreateNoteLiveDataWrapper {
    interface Read : LiveDataWrapper.Read<Pair<String,String>>
    interface Update : LiveDataWrapper.Update<Pair<String,String>>
    interface Clear {
        fun clear()
    }
    interface Mutable : Read, Update
    interface All : Mutable, Clear
    class Base : LiveDataWrapper.Abstract<Pair<String,String>>(), Mutable, All {
        override fun clear() {
            update(Pair("",""))
        }
    }
}