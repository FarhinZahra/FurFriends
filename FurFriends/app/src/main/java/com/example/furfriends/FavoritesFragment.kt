package com.example.furfriends

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.ui.favorites.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        val favoritesRecyclerView: RecyclerView = view.findViewById(R.id.rv_favorite_pets)
        favoritesRecyclerView.layoutManager = GridLayoutManager(context, 2) // 2 columns

        // Initialize the adapter with an empty list
        favoritesAdapter = FavoritesAdapter(emptyList())
        favoritesRecyclerView.adapter = favoritesAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the favoritePets LiveData from the ViewModel
        viewModel.favoritePets.observe(viewLifecycleOwner) { pets ->
            // Update the adapter with the new list of pets
            favoritesAdapter.updatePets(pets)
        }

        // Set the click listener for the adapter
        favoritesAdapter.onItemClick = {
            val intent = Intent(activity, PetDetailsActivity::class.java)
            // Pass the ID of the clicked pet to the details screen
            intent.putExtra("petId", it.id)
            startActivity(intent)
        }

        // Tell the ViewModel to load the data
        viewModel.loadFavoritePets()
    }
}
