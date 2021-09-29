package com.trigonated.gamecollection.ui.gamecollectionstatus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.trigonated.gamecollection.databinding.ListItemGamecollectionstatusBinding
import com.trigonated.gamecollection.model.objects.GameCollectionStatus

/**
 * Adapter for a list of [GameCollectionStatus].
 */
class GameCollectionStatusAdapter :
    ListAdapter<GameCollectionStatus, RecyclerView.ViewHolder>(
        GameCollectionStatusDiffCallback()
    ) {
    /** Listener for when an item's status button is clicked. */
    var itemStatusButtonClickListener: ItemStatusButtonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GameCollectionStatusHolder(
            ListItemGamecollectionstatusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as GameCollectionStatusHolder).bind(item)
    }

    inner class GameCollectionStatusHolder(
        private val binding: ListItemGamecollectionstatusBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Setup the listeners for the status buttons
            binding.setOnWishlistedButtonClickListener {
                binding.item?.let { item ->
                    itemStatusButtonClickListener?.onItemWishlistedStatusButtonClick(item)
                }
            }
            binding.setOnOwnedButtonClickListener {
                binding.item?.let { item ->
                    itemStatusButtonClickListener?.onItemOwnedStatusButtonClick(item)
                }
            }
        }

        /**
         * Bind the [item] to this ViewHolder.
         * @param item The item to bind.
         */
        fun bind(item: GameCollectionStatus) {
            binding.apply {
                this.item = item
                executePendingBindings()
            }
        }
    }

    /**
     * Listener for clicks on either of the status buttons.
     */
    interface ItemStatusButtonClickListener {
        fun onItemWishlistedStatusButtonClick(item: GameCollectionStatus)
        fun onItemOwnedStatusButtonClick(item: GameCollectionStatus)
    }
}

/**
 * Diff class for [GameCollectionStatus] objects.
 */
private class GameCollectionStatusDiffCallback :
    DiffUtil.ItemCallback<GameCollectionStatus>() {

    override fun areItemsTheSame(
        oldItem: GameCollectionStatus,
        newItem: GameCollectionStatus
    ): Boolean {
        return oldItem.platform.id == newItem.platform.id
    }

    override fun areContentsTheSame(
        oldItem: GameCollectionStatus,
        newItem: GameCollectionStatus
    ): Boolean {
        return oldItem == newItem
    }
}