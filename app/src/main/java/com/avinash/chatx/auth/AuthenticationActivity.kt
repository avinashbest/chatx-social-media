package com.avinash.chatx.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avinash.chatx.R
import com.avinash.chatx.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        * As soon as user started, taking him/her to the login fragment
        * */
        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_fragmentContainer, LoginFragment())
            .commit()
    }
}