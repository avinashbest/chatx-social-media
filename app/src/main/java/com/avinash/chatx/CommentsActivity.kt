package com.avinash.chatx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avinash.chatx.adapters.CommentsAdapter
import com.avinash.chatx.databinding.ActivityCommentsBinding
import com.avinash.chatx.models.Comment
import com.avinash.chatx.util.UserUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class CommentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentsBinding

    private var postId: String? = null
    private var commentsAdapter: CommentsAdapter? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getStringExtra("postId")
        recyclerView = binding.commentsRecyclerView

        setUpRecyclerView()

        binding.commentSendIcon.setOnClickListener {
            val commentText = binding.commentEditText.editableText.toString()
            val firestore = FirebaseFirestore.getInstance()
            val comment = Comment(commentText, UserUtil.user!!, System.currentTimeMillis())
            firestore.collection("Posts").document(postId!!)
                .collection("Comments")
                .document().set(comment)

            binding.commentEditText.editableText.clear()
        }
    }

    private fun setUpRecyclerView() {
        val firestore = FirebaseFirestore.getInstance()
        val query = postId?.let {
            firestore.collection("Posts").document(it).collection("Comments")
        }
        val recyclerViewOptions = query?.let {
            FirestoreRecyclerOptions.Builder<Comment>().setQuery(it, Comment::class.java).build()
        }
        commentsAdapter = recyclerViewOptions?.let {
            CommentsAdapter(it, this)
        }
        recyclerView.adapter = commentsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = null
    }

    override fun onStart() {
        super.onStart()
        commentsAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        commentsAdapter?.stopListening()
    }
}