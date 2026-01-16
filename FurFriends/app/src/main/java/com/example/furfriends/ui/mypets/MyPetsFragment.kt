package com.example.furfriends.ui.mypets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.R
import com.example.furfriends.ui.common.MyPetsAdapter
import com.example.furfriends.ui.pets.UploadPetActivity
import com.example.furfriends.ui.profile.ProfileViewModel
import com.example.furfriends.ui.requests.OwnerRequestsActivity

class MyPetsFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var myPetsAdapter: MyPetsAdapter
    private var selectedPetId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location, container, false)

        val myPetsRecyclerView: RecyclerView = view.findViewById(R.id.rv_my_pets_location)
        myPetsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        myPetsAdapter = MyPetsAdapter(emptyList())
        myPetsRecyclerView.adapter = myPetsAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.myPets.observe(viewLifecycleOwner) { pets ->
            myPetsAdapter.updatePets(pets)
        }

        viewModel.actionStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess { viewModel.loadProfile() }
            result.onFailure {
                Toast.makeText(requireContext(), "Action failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<android.widget.ImageView>(R.id.iv_add_pet_location).setOnClickListener {
            val intent = Intent(activity, UploadPetActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<android.widget.Button>(R.id.btn_view_requests_location).setOnClickListener {
            val intent = Intent(activity, OwnerRequestsActivity::class.java)
            startActivity(intent)
        }

        myPetsAdapter.onItemClick = { pet ->
            selectedPetId = pet.id
            view.findViewById<TextView>(R.id.tv_selected_pet_location).text = "Selected: ${pet.name}"
        }

        view.findViewById<android.widget.Button>(R.id.btn_mark_adopted_location).setOnClickListener {
            val petId = selectedPetId
            if (petId != null) {
                viewModel.markPetAdopted(petId)
            } else {
                Toast.makeText(requireContext(), "Select a pet first", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<android.widget.Button>(R.id.btn_delete_pet_location).setOnClickListener {
            val petId = selectedPetId
            if (petId != null) {
                viewModel.deletePet(petId)
            } else {
                Toast.makeText(requireContext(), "Select a pet first", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadProfile()
    }
}

