package com.example.furfriends.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.R
import com.example.furfriends.data.chat.ChatConversation
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale

class ConversationListAdapter(private var conversations: List<ChatConversation>) : RecyclerView.Adapter<ConversationListAdapter.ConversationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        holder.bind(conversation)
    }

    override fun getItemCount() = conversations.size

    fun updateConversations(newConversations: List<ChatConversation>) {
        conversations = newConversations
        notifyDataSetChanged()
    }

    class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val participantName: TextView = itemView.findViewById(R.id.tv_participant_name)
        private val lastMessage: TextView = itemView.findViewById(R.id.tv_last_message_preview)
        private val timestamp: TextView = itemView.findViewById(R.id.tv_last_message_time)
        private val avatar: ShapeableImageView = itemView.findViewById(R.id.iv_participant_avatar)

        fun bind(conversation: ChatConversation) {
            // In a real app, you would fetch the other user's name and avatar based on the participant IDs
            participantName.text = "User ${conversation.participants.firstOrNull { it != "user1" }}" // Placeholder name
            lastMessage.text = conversation.lastMessage

            // Format the timestamp
            conversation.lastMessageTimestamp?.let {
                val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                timestamp.text = sdf.format(it)
            }

            // Set a placeholder avatar
            avatar.setImageResource(R.drawable.logo)
        }
    }
}
