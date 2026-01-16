package com.example.furfriends.ui.chat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.R

class ConversationListActivity : AppCompatActivity() {

    private val viewModel: ConversationListViewModel by viewModels()
    private lateinit var conversationAdapter: ConversationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_list)

        // --- RecyclerView Setup ---
        val conversationsRecyclerView: RecyclerView = findViewById(R.id.rv_conversations)
        conversationsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty list
        conversationAdapter = ConversationListAdapter(emptyList())
        conversationsRecyclerView.adapter = conversationAdapter

        // --- Observers ---
        viewModel.conversations.observe(this) { conversations ->
            conversationAdapter.updateConversations(conversations)
        }

        // --- Click Listeners ---
        findViewById<ImageView>(R.id.iv_back_arrow_conversations).setOnClickListener {
            finish()
        }

        // TODO: Add a click listener to the adapter to open the ChatMessageActivity
        // conversationAdapter.onItemClick = { conversation ->
        //     val intent = Intent(this, ChatMessageActivity::class.java)
        //     intent.putExtra("conversationId", conversation.conversationId)
        //     startActivity(intent)
        // }

        // Tell the ViewModel to load the data (passing a dummy user ID for now)
        viewModel.loadConversations("user1")
    }
}
