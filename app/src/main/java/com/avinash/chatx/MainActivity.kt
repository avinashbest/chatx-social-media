package com.avinash.chatx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.avinash.chatx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIncrease.setOnClickListener {
            var value = Integer.parseInt(binding.textView.text.toString())
            value++
            binding.textView.text = value.toString()
        }

        binding.btnDecrease.setOnClickListener {
            var value = Integer.parseInt(binding.textView.text.toString())
            value--
            binding.textView.text = value.toString()
        }
    }

    /*
    * For saving the state in android whenever, landscape to portrait or any activity change happens
    * */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("Value", binding.textView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.textView.text = savedInstanceState.getString("Value")
    }
}