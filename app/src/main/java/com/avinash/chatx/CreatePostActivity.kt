package com.avinash.chatx

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.avinash.chatx.databinding.ActivityCreatePostBinding
import com.avinash.chatx.models.Post
import com.avinash.chatx.models.User
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CreatePostActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CreatePostActivity"
    }

    private lateinit var binding: ActivityCreatePostBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        binding.btnPost.setOnClickListener {
            val text = binding.postText.text.toString()

            if (TextUtils.isEmpty(text)) {
                Toast.makeText(
                    this,
                    "Description cannot be empty.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            addPost(text)
        }
    }

    private fun addPost(text: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Users")
//                fetching the document details from firestore
            .document(FirebaseAuth.getInstance().currentUser?.uid!!).get()
            .addOnCompleteListener {
//                changing the firebase result to User's class object
                val user = it.result?.toObject(User::class.java)
//                Storage Reference: Firebase Storage
                val storage = FirebaseStorage.getInstance().reference.child("Images")
                    .child(FirebaseAuth.getInstance().currentUser?.email.toString() + "_" + System.currentTimeMillis() + ".jpg")
//                Upload task: Passing the uri of the image file which have to be upload to the firebase storage
                val uploadTask = storage.putFile(imageUri!!)
//                Uploading to Firebase
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        Log.d("Upload Task", task.exception.toString())
                    }
                    storage.downloadUrl
                }.addOnCompleteListener { urlTaskCompleted ->
                    if (urlTaskCompleted.isSuccessful) {
                        val downloadUri = urlTaskCompleted.result
//                        Creating an object of Post data class
                        val post =
                            Post(text, downloadUri.toString(), user!!, System.currentTimeMillis())
                        firestore.collection("Posts")
                            .document()
                            .set(post)
                            .addOnCompleteListener { posted ->
                                if (posted.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Posted Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Error occurred! Please Try again.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Log.d(TAG, urlTaskCompleted.exception.toString())
                    }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*
        * Getting & Setting the Image Uri
        * */
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                binding.postImage.setImageURI(fileUri)
                imageUri = fileUri
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(
                    this,
                    ImagePicker.getError(data),
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                Toast.makeText(
                    this,
                    "Task Cancelled",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}