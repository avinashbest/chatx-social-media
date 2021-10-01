package com.avinash.chatx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avinash.chatx.adapters.ChatAdapter
import com.avinash.chatx.databinding.FragmentChatBinding
import com.avinash.chatx.models.Chat
import com.avinash.chatx.util.UserUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ChatFragment : Fragment() {

    var chatroomId: String? = null
    lateinit var chatAdapter: ChatAdapter
    lateinit var chatRecyclerView: RecyclerView

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        if (bundle != null) {
            chatroomId = bundle.getString("chatroomId")
        }

        chatRecyclerView = binding.chatRecyclerView
        setUpRecyclerView()

        binding.chatSendIcon.setOnClickListener {
            if (binding.chatEditText.text.isNullOrEmpty()) {
                return@setOnClickListener
            }
            val chatText = binding.chatEditText.text.toString()
            val firestore = FirebaseFirestore.getInstance().collection("Chatrooms")
                .document(chatroomId!!).collection("Messages")

            val chat = Chat(chatText, UserUtil.user!!, System.currentTimeMillis(), chatroomId!!)

            firestore.document().set(chat).addOnCompleteListener {
                chatRecyclerView.scrollToPosition(chatRecyclerView.adapter?.itemCount!! - 1)
                binding.chatEditText.text.clear()
            }
        }
    }

    private fun setUpRecyclerView() {
        val firestore = FirebaseFirestore.getInstance()
        val query = firestore.collection("Chatrooms").document(chatroomId!!).collection("Messages")
            .orderBy("time", Query.Direction.ASCENDING)

        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Chat>().setQuery(query, Chat::class.java).build()

        chatAdapter = ChatAdapter(recyclerViewOptions)

        chatRecyclerView.adapter = chatAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onStart() {
        super.onStart()
        chatAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        chatAdapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}