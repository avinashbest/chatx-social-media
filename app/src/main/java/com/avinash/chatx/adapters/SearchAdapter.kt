package com.avinash.chatx.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.avinash.chatx.R
import com.avinash.chatx.models.User
import com.avinash.chatx.util.UserUtil
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(options: FirestoreRecyclerOptions<User>, val context: Context) :
    FirestoreRecyclerAdapter<User, SearchAdapter.SearchViewHolder>(options) {
    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: CircleImageView = itemView.findViewById(R.id.profile_image)
        val nameText: TextView = itemView.findViewById(R.id.user_name)
        val followButton: Button = itemView.findViewById(R.id.follow_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int, model: User) {
        holder.nameText.text = model.name

        if (UserUtil.user?.following?.contains(snapshots.getSnapshot(holder.absoluteAdapterPosition).id)!!) {
            holder.followButton.text = context.getString(R.string.following)
        } else {
            holder.followButton.text = context.getString(R.string.follow)
        }

//        Follow Button Logic

        holder.followButton.setOnClickListener {
            val firestore = FirebaseFirestore.getInstance()
            val userDocument = firestore.collection("Users").document(UserUtil.user?.id!!)

            userDocument.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result?.toObject(User::class.java)
                    if (holder.followButton.text == "Following") {
                        user?.following?.remove(snapshots.getSnapshot(holder.absoluteAdapterPosition).id)
                        user?.let { _user ->
                            userDocument.set(_user)
                        }
                        holder.followButton.text = context.getString(R.string.follow)
                    } else {
                        user?.following?.add(snapshots.getSnapshot(holder.absoluteAdapterPosition).id)
                        user?.let { _user ->
                            userDocument.set(_user)
                        }
                        holder.followButton.text = context.getString(R.string.following)
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}