package com.example.notesapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.notesapp.R
import com.example.notesapp.core.App
import com.example.notesapp.core.ProvideViewModel
import com.example.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProvideViewModel {
    private lateinit var viewModel: MainViewModel
    private lateinit var b : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        viewModel = viewModel(MainViewModel::class.java)
        viewModel.liveData().observe(this) {
            it.show(supportFragmentManager, R.id.container)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T =
        (application as App).viewModel(viewModelClass)
}