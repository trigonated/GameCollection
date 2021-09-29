package com.trigonated.gamecollection.ui.generic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.trigonated.gamecollection.databinding.ListItemGameBinding
import com.trigonated.gamecollection.model.objects.Game
import com.trigonated.gamecollection.ui.home.HomeFragmentDirections

/**
 * Adapter for a list of [Game].
 */
class GameAdapter : ListAdapter<Game, RecyclerView.ViewHolder>(GameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GameViewHolder(
            ListItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as GameViewHolder).bind(item)
    }

    class GameViewHolder(
        private val binding: ListItemGameBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Setup the click listener
            binding.setOnClickListener { view ->
                binding.game?.let { game ->
                    navigateToGameDetails(view, game.id)
                }
            }
        }

        /**
         * Bind the [item] to this ViewHolder.
         * @param item The item to bind.
         */
        fun bind(item: Game) {
            binding.apply {
                game = item
                executePendingBindings()
            }
        }

        private fun navigateToGameDetails(view: View, gameId: String) {
            val direction =
                HomeFragmentDirections.actionHomeFragmentToGameDetailsFragment(gameId = gameId)
            view.findNavController().navigate(direction)
        }
    }
}

/**
 * Diff class for [Game] objects.
 */
private class GameDiffCallback : DiffUtil.ItemCallback<Game>() {

    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Game,
        newItem: Game
    ): Boolean {
        return oldItem == newItem
    }
}