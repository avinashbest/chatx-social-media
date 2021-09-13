package com.avinash.chatx

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.avinash.chatx.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupTextView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_fragmentContainer, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.emailText.editText?.text.toString()
            val password = binding.passwordText.editText?.text.toString()

            /*
            * Checking whether the email field is empty or not?
            * If not empty then valid or not?
            * */
            if (TextUtils.isEmpty(email)) {
                binding.emailText.error = "Email is required."
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailText.error = "Enter a valid email address"
                return@setOnClickListener
            }

            /*
            * Checking whether the password field is empty or not?
            * If not empty then valid or not?
            * */
            if (TextUtils.isEmpty(password)) {
                binding.passwordText.error = "Password is required."
                return@setOnClickListener
            }

            binding.loginProgressBar.visibility = View.VISIBLE

            /*
            * Authentication for Firebase
            * */
            val auth = FirebaseAuth.getInstance()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        startActivity(Intent(activity, MainActivity::class.java))
                    else {
                        Toast.makeText(
                            context,
                            "Something went wrong! Please try again.",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(TAG, task.exception.toString())
                    }
                }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}