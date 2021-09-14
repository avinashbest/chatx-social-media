package com.avinash.chatx.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.avinash.chatx.R
import com.avinash.chatx.models.Post
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.thunder413.datetimeutils.DateTimeStyle
import com.github.thunder413.datetimeutils.DateTimeUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/*
* Firestore Recycler Adapter for FeedFragment
* */
class FeedAdapter(options: FirestoreRecyclerOptions<Post>, val context: Context) :
    FirestoreRecyclerAdapter<Post, FeedAdapter.FeedViewHolder>(options) {

    class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage: ImageView = itemView.findViewById(R.id.feed_post_image)
        val likeIcon: ImageView = itemView.findViewById(R.id.post_like_btn)
        val commentIcon: ImageView = itemView.findViewById(R.id.post_comment_btn)
        val postLikeCount: TextView = itemView.findViewById(R.id.like_count)
        val postCommentCount: TextView = itemView.findViewById(R.id.comment_count)
        val authorText: TextView = itemView.findViewById(R.id.post_author)
        val timeText: TextView = itemView.findViewById(R.id.post_time)
        val postText: TextView = itemView.findViewById(R.id.post_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int, model: Post) {
        val date = DateTimeUtils.formatDate(model.time)
        val dateFormatted = DateTimeUtils.formatWithStyle(date, DateTimeStyle.LONG)

        holder.postText.text = model.text
        holder.authorText.text = model.user.name
        holder.timeText.text = dateFormatted
        holder.postLikeCount.text = model.likeList.size.toString()

        Glide.with(context)
            .load(model.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(holder.postImage)

        val firestore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val postDocument =
            firestore.collection("Posts").document(snapshots.getSnapshot(holder.adapterPosition).id)

        postDocument.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val post = it.result?.toObject(Post::class.java)
                post?.likeList?.let { list ->
                    if (list.contains(userId)) {
                        holder.likeIcon.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.icon_like_fill
                            )
                        )
                    } else {
                        holder.likeIcon.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.like_icon_outline
                            )
                        )
                    }
//                    Like Feature of Feed
                    holder.likeIcon.setOnClickListener {
                        if (post.likeList.contains(userId)) {
                            post.likeList.remove(userId)
                            holder.likeIcon.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.like_icon_outline
                                )
                            )
                        } else {
                            userId?.let { userId ->
                                post.likeList.add(userId)
                            }
                            holder.likeIcon.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.icon_like_fill
                                )
                            )
                        }
                    }
//                    Setting the post details of current user: Updated One
                    postDocument.set(post)
                }
            } else {
                Toast.makeText(
                    context,
                    "Something went wrong! Please Try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}