package com.example.furfriends

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.ui.location.LocationViewModel

class LocationFragment : Fragment() {

    private val viewModel: LocationViewModel by viewModels()
    private lateinit var locationsAdapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location, container, false)

        val locationsRecyclerView: RecyclerView = view.findViewById(R.id.rv_locations)
        locationsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter with an empty list
        locationsAdapter = LocationAdapter(emptyList())
        locationsRecyclerView.adapter = locationsAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the locations LiveData from the ViewModel
        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            // Update the adapter with the new list of locations
            locationsAdapter.updateLocations(locations)
        }

        // Set the click listener for the adapter
        locationsAdapter.onItemClick = {
            val intent = Intent(activity, LocationWithPetsActivity::class.java)
            // Pass the ID of the clicked location to the details screen
            intent.putExtra("locationId", it.id)
            startActivity(intent)
        }

        // Tell the ViewModel to load the data
        viewModel.loadLocations()
    }
}
