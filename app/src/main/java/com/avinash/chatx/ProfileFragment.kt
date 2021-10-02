package com.avinash.chatx

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.avinash.chatx.auth.AuthenticationActivity
import com.avinash.chatx.databinding.FragmentProfileBinding
import com.avinash.chatx.models.User
import com.avinash.chatx.util.UserUtil
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userName.setText(UserUtil.user?.name)
        binding.userBio.setText(UserUtil.user?.bio)

        binding.userImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        Glide.with(requireContext())
            .load(UserUtil.user?.imageUrl)
            .placeholder(R.drawable.person_icon_black)
            .centerCrop()
            .into(binding.userImage)

        binding.btnSave.setOnClickListener {
            val newUserName = binding.userName.text.toString()
            val newBio = binding.userBio.text.toString()

            if (newUserName.isEmpty()) {
                Toast.makeText(context, "Name field is required.", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            val userDocument = FirebaseFirestore.getInstance().collection("Users")
                .document(UserUtil.user?.id!!)

            val user = User(
                id = UserUtil.user?.id!!,
                name = newUserName,
                email = UserUtil.user?.email!!,
                following = UserUtil.user?.following!!,
                bio = newBio,
                imageUrl = UserUtil.user?.imageUrl!!
            )

            userDocument.set(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Details Updated", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }
            }

            UserUtil.getCurrentUser()
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            context?.startActivity(Intent(activity, AuthenticationActivity::class.java))
            activity?.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            binding.userImage.setImageURI(fileUri)
            imageUri = fileUri
            addUserImage()
        } else if (requestCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun addUserImage() {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("Users")
            .document(UserUtil.user?.id!!)
            .get().addOnCompleteListener {
                val storage = FirebaseStorage.getInstance().reference.child("Images")
                    .child(UserUtil.user?.email.toString() + "_" + System.currentTimeMillis() + ".jpg")

                val uploadTask = storage.putFile(imageUri!!)

                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        Log.d("Upload Task", task.exception.toString())
                    }
                    storage.downloadUrl
                }.addOnCompleteListener { urlTaskCompleted ->
                    if (urlTaskCompleted.isSuccessful) {
                        val downloadUri = urlTaskCompleted.result

                        val newUser = User(
                            id = UserUtil.user?.id!!,
                            name = UserUtil.user?.name!!,
                            email = UserUtil.user?.email!!,
                            following = UserUtil.user?.following!!,
                            bio = UserUtil.user?.bio!!,
                            imageUrl = downloadUri.toString()
                        )

                        firestore.collection("Users").document(UserUtil.user?.id!!).set(newUser)
                            .addOnCompleteListener { saved ->
                                if (saved.isSuccessful) {
                                    UserUtil.getCurrentUser()
                                    Toast.makeText(context, "Image Uploaded", Toast.LENGTH_LONG)
                                        .show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Something went wrong.",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }
}