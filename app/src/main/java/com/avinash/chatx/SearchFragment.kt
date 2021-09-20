package com.avinash.chatx

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avinash.chatx.adapters.SearchAdapter
import com.avinash.chatx.databinding.FragmentSearchBinding
import com.avinash.chatx.models.User
import com.avinash.chatx.util.UserUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class SearchFragment : Fragment() {

    lateinit var adapter: SearchAdapter
    lateinit var recyclerView: RecyclerView

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchToolbar.title = "Search Users"
        (activity as? MainActivity)?.setSupportActionBar(binding.searchToolbar)
        (activity as? MainActivity)?.supportActionBar?.show()

        setHasOptionsMenu(true)

        val firestore = FirebaseFirestore.getInstance()
        val query = firestore.collection("Users")
            .whereNotEqualTo("id", FirebaseAuth.getInstance().currentUser?.uid)

        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java).build()

        adapter = SearchAdapter(recyclerViewOptions, requireContext())

        recyclerView = binding.searchRecyclerView

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.search_menu, menu)

        val searchView = SearchView(context)
        menu.findItem(R.id.action_search).actionView = searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerView.visibility = View.VISIBLE
                val firestore = FirebaseFirestore.getInstance()
                val newQuery = firestore.collection("Users")
                    .whereEqualTo("name", query)
                    .whereNotEqualTo("id", UserUtil.user?.id) // Excluding current user from search

                val newRecyclerViewOptions =
                    FirestoreRecyclerOptions.Builder<User>().setQuery(newQuery, User::class.java)
                        .build()

                adapter.updateOptions(newRecyclerViewOptions)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerView.visibility = View.INVISIBLE
                return false
            }

        })
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