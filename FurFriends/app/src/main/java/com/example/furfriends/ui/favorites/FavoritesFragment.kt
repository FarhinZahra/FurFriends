package com.example.furfriends.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.R
import com.example.furfriends.ui.pets.PetDetailsActivity

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
        favoritesRecyclerView.layoutManager = LinearLayoutManager(context)

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
            view.findViewById<android.widget.ProgressBar>(R.id.pb_favorites_loading).visibility = View.GONE
            view.findViewById<android.widget.TextView>(R.id.tv_favorites_count).text = "${pets.size} saved"
            view.findViewById<android.widget.LinearLayout>(R.id.ll_favorites_empty).visibility =
                if (pets.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.toggleStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                viewModel.loadFavoritePets()
            }
        }

        // Set the click listener for the adapter
        favoritesAdapter.onItemClick = {
            val intent = Intent(activity, PetDetailsActivity::class.java)
            // Pass the ID of the clicked pet to the details screen
            intent.putExtra("petId", it.id)
            startActivity(intent)
        }
        favoritesAdapter.onFavoriteToggle = { pet ->
            viewModel.toggleFavorite(pet.id)
        }

        // Tell the ViewModel to load the data
        view.findViewById<android.widget.ProgressBar>(R.id.pb_favorites_loading).visibility = View.VISIBLE
        view.findViewById<android.widget.LinearLayout>(R.id.ll_favorites_empty).visibility = View.GONE
        viewModel.loadFavoritePets()

        view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_favorites_explore)
            .setOnClickListener {
                findNavController().navigate(R.id.navigation_home)
            }
    }

    override fun onResume() {
        super.onResume()
        view?.findViewById<android.widget.ProgressBar>(R.id.pb_favorites_loading)?.visibility = View.VISIBLE
        view?.findViewById<android.widget.LinearLayout>(R.id.ll_favorites_empty)?.visibility = View.GONE
        viewModel.loadFavoritePets()
    }
}

