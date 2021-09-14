package com.avinash.chatx

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avinash.chatx.adapters.FeedAdapter
import com.avinash.chatx.databinding.FragmentFeedBinding
import com.avinash.chatx.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore


class FeedFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FeedAdapter

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            startActivity(Intent(context, CreatePostActivity::class.java))
        }

        recyclerView = binding.feedRecyclerView

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val firestore = FirebaseFirestore.getInstance()
        val query = firestore.collection("Posts")

        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        context?.let {
            adapter = FeedAdapter(recyclerViewOptions, it)
        }

        if (this::adapter.isInitialized) {
            recyclerView.adapter = adapter
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}