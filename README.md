# ChatX - v1.0:

- It is a simple, reliable, fun and creative social network service.
- It is a valuable communication tool with others locally and worldwide, as well as to share, create, and spread information.
- ChatX allows you to share your photos and videos with your friends, families and followers.
- It also provides a chatroom for secure messaging. Multiple chatrooms can be created by a user.
- Users can like and comment on photos and follow other users to add their content to a personal feed.

# Preview:

<p float="left">
  <img src="preview/1.jpeg" width="190" />
  <img src="preview/2.jpeg" width="190" /> 
  <img src="preview/3.jpeg" width="190" />
  <img src="preview/4.jpeg" width="190" />
  <img src="preview/5.jpeg" width="190" />
  <img src="preview/6.jpeg" width="190" />
  <img src="preview/7.jpeg" width="190" />
  <img src="preview/8.jpeg" width="190" />
</p>

# Functionalities:

# Tools/Technologies Used:

- Kotlin
- FirebaseAuth (Authentication)
- Firebase Firestore Database (Non-Relational Database)
- Firebase Storage
- Firebase UI: FirebaseRecyclerView

# Project Structure:

```structure
- [:app]
    - com.avinash.chatx
        - adapters
            - ChatAdapter
            - ChatroomAdapter
            - CommentsAdapter
            - FeedAdapter
            - SearchAdapter
        - auth
            - AuthenticationActivity
            - LoginFragment
            - RegisterFragment
        - models
            - Chat
            - Chatroom
            - Comment
            - Post
            - User
        - util
            - UserUtil
        - ChatFragment
        - ChatroomFragment
        - CommentsActivity
        - CreatePostActivity
        - FeedFragment
        - MainActivity
        - ProfileActivity
        - SearchFragment
    - res
        - drawable
            - add_icon.xml
            - chat_icon.xml
            - chat_text_other_shape.xml
            - chat_text_self_shape.xml
            - chatx_logo.png
            - comment_background.xml
            - comment_icon.xml
            - feed_icon.xml
            - icon_like_fill.xml
            - like_icon_outline.xml
            - lock_icon.xml
            - person_icon.xml
            - person_icon_black.xml
            - placeholder_image.xml
            - search_icon.xml
            - send_icon.xml
        - layout
            - activity_authentication.xml
            - activity_comments.xml
            - activity_create_post.xml
            - activity_main.xml
            - fragment_chat.xml
            - fragment_chatroom.xml
            - fragment_feed.xml
            - fragment_login.xml
            - fragment_profile.xml
            - fragment_register.xml
            - fragment_search.xml
            - item_chat_other.xml
            - item_chat_self.xml
            - item_chatroom.xml
            - item_comments.xml
            - item_post.xml
            - item_user.xml
        - menu
            - bottom_nav_view.xml
            - search_menu.xml
        - minmap
            - ic_launcher.png
                - ic_launcher.png
                - ic_launcher.png
                - ic_launcher.xml
                - ic_launcher.png
                - ic_launcher.png
            - ic_launcher_foreground.png
                - ic_launcher_foreground.png
                - ic_launcher_foreground.png
                - ic_launcher_foreground.png
                - ic_launcher_foreground.png
            - ic_launcher_round.png
                - ic_launcher_round.png
                - ic_launcher_round.png
                - ic_launcher_round.png
                - ic_launcher_round.xml
                - ic_launcher_round.png
        - values
            - colors.xml
            - ic_launcher_background.xml
            - strings.xml
            - themes.xml
- Gradle Scripts
    - Build.gradle [Project]
    - Build.gradle [Module]
    - gradle-wrapper.properties
    - proguard-rules.pro
    - gradle.properties
    - settings.gradle
    - local.properties
```

# Live Working Preview:

# Credits:

- [Avinash Kumar](https://github.com/avinashbest)
