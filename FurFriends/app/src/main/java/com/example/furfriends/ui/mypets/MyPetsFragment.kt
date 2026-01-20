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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.R
import com.example.furfriends.ui.common.MyPetsAdapter
import com.example.furfriends.ui.pets.PetEditActivity
import com.example.furfriends.ui.pets.UploadPetActivity
import com.example.furfriends.ui.profile.ProfileViewModel
import com.example.furfriends.ui.requests.OwnerRequestsActivity
import com.google.android.material.chip.Chip

class MyPetsFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var myPetsAdapter: MyPetsAdapter
    private var allPets: List<com.example.furfriends.data.Pet> = emptyList()
    private var activeTab: String = "Active"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location, container, false)

        val myPetsRecyclerView: RecyclerView = view.findViewById(R.id.rv_my_pets_location)
        myPetsRecyclerView.layoutManager = GridLayoutManager(context, 2)

        myPetsAdapter = MyPetsAdapter(emptyList())
        myPetsRecyclerView.adapter = myPetsAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.myPets.observe(viewLifecycleOwner) { pets ->
            allPets = pets
            updateHeaderCount(view)
            applyFilter(view)
            view.findViewById<android.widget.ProgressBar>(R.id.pb_my_pets_loading).visibility = View.GONE
        }

        viewModel.actionStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess { viewModel.loadProfile() }
            result.onFailure {
                Toast.makeText(requireContext(), "Action failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_add_pet).setOnClickListener {
            val intent = Intent(activity, UploadPetActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_empty_add).setOnClickListener {
            val intent = Intent(activity, UploadPetActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<android.widget.Button>(R.id.btn_view_requests_location).setOnClickListener {
            val intent = Intent(activity, OwnerRequestsActivity::class.java)
            startActivity(intent)
        }

        myPetsAdapter.onEditClick = { pet ->
            val intent = Intent(activity, PetEditActivity::class.java)
            intent.putExtra("petId", pet.id)
            startActivity(intent)
        }

        myPetsAdapter.onMarkAdoptedClick = { pet ->
            viewModel.markPetAdopted(pet.id)
        }

        myPetsAdapter.onDeleteClick = { pet ->
            viewModel.deletePet(pet.id)
        }

        view.findViewById<Chip>(R.id.chip_my_pets_active).setOnClickListener {
            activeTab = "Active"
            applyFilter(view)
        }
        view.findViewById<Chip>(R.id.chip_my_pets_adopted).setOnClickListener {
            activeTab = "Adopted"
            applyFilter(view)
        }
        view.findViewById<Chip>(R.id.chip_my_pets_requests).setOnClickListener {
            activeTab = "Requests"
            applyFilter(view)
        }

        view.findViewById<android.widget.ProgressBar>(R.id.pb_my_pets_loading).visibility = View.VISIBLE
        view.findViewById<android.widget.LinearLayout>(R.id.ll_my_pets_empty).visibility = View.GONE
        viewModel.loadProfile()
    }

    private fun applyFilter(view: View) {
        val listView: RecyclerView = view.findViewById(R.id.rv_my_pets_location)
        val emptyView: android.widget.LinearLayout = view.findViewById(R.id.ll_my_pets_empty)
        val requestsPanel: android.view.View = view.findViewById(R.id.card_requests_panel)

        if (activeTab == "Requests") {
            listView.visibility = View.GONE
            emptyView.visibility = View.GONE
            requestsPanel.visibility = View.VISIBLE
            return
        }

        val filtered = when (activeTab) {
            "Adopted" -> allPets.filter { it.status.equals("adopted", ignoreCase = true) }
            else -> allPets.filter { !it.status.equals("adopted", ignoreCase = true) }
        }
        requestsPanel.visibility = View.GONE
        listView.visibility = View.VISIBLE
        myPetsAdapter.updatePets(filtered)
        emptyView.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateHeaderCount(view: View) {
        val activeCount = allPets.count { !it.status.equals("adopted", ignoreCase = true) }
        view.findViewById<TextView>(R.id.tv_my_pets_count).text = "$activeCount active"
    }
}

