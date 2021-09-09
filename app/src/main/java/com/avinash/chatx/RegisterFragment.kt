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
import com.avinash.chatx.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {

    companion object {
        const val TAG = "RegisterFragment"
    }

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginTextView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_fragmentContainer, LoginFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.emailText.editText?.text.toString()
            val name = binding.nameText.editText?.text.toString()
            val password = binding.passwordText.editText?.text.toString()
            val confirmPassword = binding.confirmPasswordText.editText?.text.toString()

            /*
            * Setting all the input fields error to null.
            * */
            binding.emailText.error = null
            binding.nameText.error = null
            binding.passwordText.error = null
            binding.confirmPasswordText.error = null

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
            * Checking whether the name field is empty or not?
            * */
            if (TextUtils.isEmpty(name)) {
                binding.nameText.error = "Name is required."
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

            if (!password.matches(passwordRegex)) {
                binding.passwordText.error =
                    "Password should contains minimum 8 character, at least one uppercase letter, one lowercase letter and one number:"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                binding.confirmPasswordText.error = "Confirm Password is required"
                return@setOnClickListener
            }

            /*
            * If password is not equal to confirmPassword
            * */
            if (password != confirmPassword) {
                binding.confirmPasswordText.error = "Password do not match"
                return@setOnClickListener
            }

            binding.signupProgressBar.visibility = View.VISIBLE

            /*
            * Authentication for Firebase
            * */

            val auth = FirebaseAuth.getInstance()

            /*
            * Creating an user on firebase using email and password field
            * */
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                        creating the object of User data class and passing the data to cloud firestore
                        val user = User(auth.currentUser?.uid!!, name, email)
//                        instance of firestore cloud storage
                        val firestore = FirebaseFirestore.getInstance().collection("Users")
//                        setting the user's details in new collection "Users"
                        firestore.document(auth.currentUser?.uid!!).set(user)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    startActivity(Intent(activity, MainActivity::class.java))
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Something went wrong, Please Try again.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.d(TAG, it.exception.toString())
                                }
                            }
                        binding.signupProgressBar.visibility = View.GONE
                    } else {
                        binding.signupProgressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Something went wrong, Please Try again.",
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