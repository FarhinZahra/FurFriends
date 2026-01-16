package com.example.furfriends.ui.requests

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.R

class OwnerRequestsActivity : AppCompatActivity() {

    private val viewModel: OwnerRequestsViewModel by viewModels()
    private lateinit var adapter: OwnerRequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_requests)

        findViewById<ImageView>(R.id.iv_back_arrow_requests).setOnClickListener { finish() }

        val recyclerView: RecyclerView = findViewById(R.id.rv_requests)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = OwnerRequestsAdapter(emptyList())
        recyclerView.adapter = adapter

        adapter.onApprove = { request -> viewModel.approve(request) }
        adapter.onReject = { request -> viewModel.reject(request) }
        adapter.onCall = { request -> openDialer(request.requesterPhone) }

        viewModel.requests.observe(this) { requests ->
            adapter.updateRequests(requests)
        }

        viewModel.actionStatus.observe(this) { result ->
            result.onSuccess { viewModel.loadRequests() }
            result.onFailure {
                Toast.makeText(this, "Action failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadRequests()
    }

    private fun openDialer(phone: String) {
        if (phone.isBlank()) {
            Toast.makeText(this, "Phone not available", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        startActivity(intent)
    }
}
