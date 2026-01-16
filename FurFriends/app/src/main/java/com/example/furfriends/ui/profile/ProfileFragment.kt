package com.example.furfriends.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.furfriends.R
import com.example.furfriends.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userProfile.observe(viewLifecycleOwner) { user ->
            view.findViewById<TextView>(R.id.tv_profile_name).text = user.name
            view.findViewById<TextView>(R.id.tv_profile_email).text = user.email
            view.findViewById<TextView>(R.id.tv_profile_bio).text = user.address.ifBlank { "Dhaka, Bangladesh" }
            view.findViewById<TextView>(R.id.tv_location_value).text = user.address
            view.findViewById<TextView>(R.id.tv_contact_value).text = user.phone
        }

        viewModel.postsCount.observe(viewLifecycleOwner) { count ->
            view.findViewById<TextView>(R.id.tv_stat_posts_value).text = count.toString()
        }

        viewModel.requestsCount.observe(viewLifecycleOwner) { count ->
            view.findViewById<TextView>(R.id.tv_stat_requests_value).text = count.toString()
        }

        viewModel.favoritesCount.observe(viewLifecycleOwner) { count ->
            view.findViewById<TextView>(R.id.tv_stat_favorites_value).text = count.toString()
        }

        view.findViewById<ImageView>(R.id.iv_edit_profile).setOnClickListener {
            val intent = Intent(activity, ProfileEditActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<android.widget.Button>(R.id.btn_logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        viewModel.loadProfile()
    }
}

