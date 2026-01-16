package com.example.furfriends.ui.requests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.R
import com.example.furfriends.data.AdoptionRequest

class OwnerRequestsAdapter(private var requests: List<AdoptionRequest>) :
    RecyclerView.Adapter<OwnerRequestsAdapter.RequestViewHolder>() {

    var onApprove: ((AdoptionRequest) -> Unit)? = null
    var onReject: ((AdoptionRequest) -> Unit)? = null
    var onCall: ((AdoptionRequest) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adoption_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]
        holder.bind(request)
        holder.approveButton.setOnClickListener { onApprove?.invoke(request) }
        holder.rejectButton.setOnClickListener { onReject?.invoke(request) }
        holder.callButton.setOnClickListener { onCall?.invoke(request) }
    }

    override fun getItemCount() = requests.size

    fun updateRequests(newRequests: List<AdoptionRequest>) {
        requests = newRequests
        notifyDataSetChanged()
    }

    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petNameText: TextView = itemView.findViewById(R.id.tv_request_pet_name)
        private val requesterNameText: TextView = itemView.findViewById(R.id.tv_requester_name)
        private val requesterPhoneText: TextView = itemView.findViewById(R.id.tv_requester_phone)
        private val statusText: TextView = itemView.findViewById(R.id.tv_request_status)
        val callButton: Button = itemView.findViewById(R.id.btn_request_call)
        val approveButton: Button = itemView.findViewById(R.id.btn_request_approve)
        val rejectButton: Button = itemView.findViewById(R.id.btn_request_reject)

        fun bind(request: AdoptionRequest) {
            petNameText.text = request.petName
            requesterNameText.text = "Requested by: ${request.requesterName}"
            requesterPhoneText.text = "Phone: ${request.requesterPhone}"
            statusText.text = "Status: ${request.status}"
        }
    }
}
