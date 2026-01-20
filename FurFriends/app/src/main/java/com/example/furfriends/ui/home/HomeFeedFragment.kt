package com.example.furfriends.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.ui.pets.PetDetailsActivity
import com.example.furfriends.R
import com.google.android.material.chip.Chip

class HomeFeedFragment : Fragment() {

    private val viewModel: HomeFeedViewModel by viewModels()
    private lateinit var feedAdapter: HomeFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_feed, container, false)

        val feedRecyclerView: RecyclerView = view.findViewById(R.id.rv_pet_feed)
        feedRecyclerView.layoutManager = LinearLayoutManager(context)
        feedAdapter = HomeFeedAdapter(emptyList())
        feedRecyclerView.adapter = feedAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.feedPets.observe(viewLifecycleOwner) { pets ->
            feedAdapter.updatePets(pets)
            view.findViewById<android.widget.TextView>(R.id.tv_feed_empty).visibility =
                if (pets.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.favoriteStatus.observe(viewLifecycleOwner) { result ->
            result.onFailure {
                Toast.makeText(requireContext(), "Favorite failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
            result.onSuccess {
                viewModel.loadFavorites()
            }
        }

        viewModel.favoriteIds.observe(viewLifecycleOwner) { ids ->
            feedAdapter.setFavoriteIds(ids)
        }

        viewModel.requestStatuses.observe(viewLifecycleOwner) { statuses ->
            feedAdapter.setRequestStatuses(statuses)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            view.findViewById<android.widget.ProgressBar>(R.id.pb_feed_loading).visibility =
                if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                view.findViewById<android.widget.TextView>(R.id.tv_feed_empty).visibility = View.GONE
            }
        }

        view.findViewById<Chip>(R.id.chip_all).setOnClickListener {
            viewModel.setFilter("All")
        }
        view.findViewById<Chip>(R.id.chip_dog).setOnClickListener {
            viewModel.setFilter("Dog")
        }
        view.findViewById<Chip>(R.id.chip_cat).setOnClickListener {
            viewModel.setFilter("Cat")
        }
        view.findViewById<Chip>(R.id.chip_other).setOnClickListener {
            viewModel.setFilter("Other")
        }

        feedAdapter.onItemClick = { pet ->
            val intent = Intent(activity, PetDetailsActivity::class.java)
            intent.putExtra("petId", pet.id)
            startActivity(intent)
        }
        feedAdapter.onFavoriteToggle = { pet ->
            viewModel.toggleFavorite(pet.id)
        }

        view.findViewById<android.widget.ImageView>(R.id.iv_header_favorite).setOnClickListener {
            findNavController().navigate(R.id.navigation_favorites)
        }
        view.findViewById<android.widget.ImageView>(R.id.iv_header_profile).setOnClickListener {
            findNavController().navigate(R.id.navigation_profile)
        }

        viewModel.loadFeed()
        viewModel.loadFavorites()
        viewModel.loadRequestStatuses()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFeed()
        viewModel.loadFavorites()
        viewModel.loadRequestStatuses()
    }
}
