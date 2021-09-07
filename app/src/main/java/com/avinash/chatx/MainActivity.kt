package com.avinash.chatx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avinash.chatx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}