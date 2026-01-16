package com.example.furfriends.ui.chat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.R
import com.example.furfriends.data.chat.ChatMessage

class ChatMessageActivity : AppCompatActivity() {

    private val viewModel: ChatMessageViewModel by viewModels()
    private lateinit var messageAdapter: ChatMessageAdapter
    private lateinit var conversationId: String
    private val currentUserId = "user1" // This would be fetched from your auth system

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_message)

        // Get the conversation ID from the intent
        conversationId = intent.getStringExtra("conversationId") ?: return

        // --- Toolbar Setup ---
        val toolbar: Toolbar = findViewById(R.id.toolbar_chat)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // --- RecyclerView Setup ---
        val messagesRecyclerView: RecyclerView = findViewById(R.id.rv_messages)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true // Start from the bottom
        messagesRecyclerView.layoutManager = layoutManager

        messageAdapter = ChatMessageAdapter(emptyList(), currentUserId)
        messagesRecyclerView.adapter = messageAdapter

        // --- Observers ---
        observeMessages()
        observeSendStatus()

        // --- Click Listeners ---
        findViewById<ImageButton>(R.id.btn_send_message).setOnClickListener {
            sendMessage()
        }

        // Tell the ViewModel to load the messages
        viewModel.loadMessages(conversationId)
    }

    private fun observeMessages() {
        viewModel.messages.observe(this) { messages ->
            messageAdapter.updateMessages(messages)
            findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(messages.size - 1)
        }
    }

    private fun observeSendStatus() {
        viewModel.sendStatus.observe(this) { result ->
            result.onSuccess {
                findViewById<EditText>(R.id.et_message_input).text.clear()
            }
            result.onFailure {
                Toast.makeText(this, "Failed to send message: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendMessage() {
        val messageText = findViewById<EditText>(R.id.et_message_input).text.toString().trim()
        if (messageText.isEmpty()) return

        val message = ChatMessage(
            senderId = currentUserId,
            message = messageText
            // ReceiverId and other fields would be set here
        )

        viewModel.sendMessage(conversationId, message)
    }
}
