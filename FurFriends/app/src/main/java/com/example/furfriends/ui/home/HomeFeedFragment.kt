package com.example.furfriends.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furfriends.ui.pets.PetDetailsActivity
import com.example.furfriends.R

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

        feedAdapter.onItemClick = { pet ->
            val intent = Intent(activity, PetDetailsActivity::class.java)
            intent.putExtra("petId", pet.id)
            startActivity(intent)
        }
        feedAdapter.onFavoriteToggle = { pet ->
            viewModel.toggleFavorite(pet.id)
        }

        viewModel.loadFeed()
        viewModel.loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFeed()
        viewModel.loadFavorites()
    }
}
