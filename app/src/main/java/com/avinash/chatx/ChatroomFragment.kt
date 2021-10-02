package com.avinash.chatx

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avinash.chatx.adapters.ChatroomAdapter
import com.avinash.chatx.databinding.FragmentChatroomBinding
import com.avinash.chatx.models.Chatroom
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ChatroomFragment : Fragment() {

    lateinit var chatroomAdapter: ChatroomAdapter
    lateinit var chatroomRecyclerView: RecyclerView

    private var _binding: FragmentChatroomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatroomRecyclerView = binding.chatroomRecyclerView

        setUpRecyclerView()

//        Dialog Box for Chatroom
        binding.createChatroom.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)

            val editText = EditText(context)
            alertDialog.setTitle("Create Chatroom")
            alertDialog.setMessage("Enter the name of the new chatroom that you want to create")

            alertDialog.setView(editText)

            var textEntered = ""

            alertDialog.setPositiveButton("Create") { dialogInterface, i ->
                textEntered = editText.text.toString()
                val document = FirebaseFirestore.getInstance().collection("Chatrooms").document()
                val chatroom = Chatroom(textEntered, document.id)

                document.set(chatroom)
            }

            alertDialog.setNegativeButton("Cancel") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            alertDialog.show()
        }
    }

    private fun setUpRecyclerView() {
        val firestore = FirebaseFirestore.getInstance()
        val query = firestore.collection("Chatrooms")
            .orderBy("name", Query.Direction.ASCENDING)

        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Chatroom>().setQuery(query, Chatroom::class.java)
                .build()

        chatroomAdapter = ChatroomAdapter(recyclerViewOptions, requireActivity())

        chatroomRecyclerView.adapter = chatroomAdapter
        chatroomRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onStart() {
        super.onStart()
        chatroomAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        chatroomAdapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}