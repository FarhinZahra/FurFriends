package com.example.furfriends

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.ui.profile.ProfileViewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var myPetsAdapter: MyPetsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val myPetsRecyclerView: RecyclerView = view.findViewById(R.id.rv_my_pets)
        myPetsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Initialize the adapter with an empty list
        myPetsAdapter = MyPetsAdapter(emptyList())
        myPetsRecyclerView.adapter = myPetsAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Observers ---
        viewModel.userProfile.observe(viewLifecycleOwner) { user ->
            view.findViewById<TextView>(R.id.tv_profile_name).text = user.name
            view.findViewById<TextView>(R.id.tv_profile_bio).text = "I am a very well-pleasing one who spends time with the animals." // Placeholder bio
            view.findViewById<TextView>(R.id.tv_location_value).text = user.address
            view.findViewById<TextView>(R.id.tv_contact_value).text = user.phone
            view.findViewById<TextView>(R.id.tv_email_value).text = user.email
        }

        viewModel.myPets.observe(viewLifecycleOwner) { pets ->
            myPetsAdapter.updatePets(pets)
        }

        // --- Click Listeners ---
        view.findViewById<ImageView>(R.id.iv_edit_profile).setOnClickListener {
            val intent = Intent(activity, ProfileEditActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<ImageView>(R.id.iv_add_pet).setOnClickListener {
            val intent = Intent(activity, UploadPetActivity::class.java)
            startActivity(intent)
        }

        // Tell the ViewModel to load the data
        viewModel.loadProfile()
    }
}
