package com.example.notesapp.edit

import com.example.notesapp.core.LiveDataWrapper
import com.example.notesapp.core.MyNote

interface NoteLiveDataWrapper {
    interface Read : LiveDataWrapper.Read<MyNote>
    interface Update : LiveDataWrapper.Update<MyNote>
    interface Mutable : Read,Update
    class Base : LiveDataWrapper.Abstract<MyNote>()
}
