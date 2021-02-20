package com.sealstudios.spacex.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.sealstudios.spacex.databinding.LaunchViewHolderBinding
import com.sealstudios.spacex.objects.LaunchResponse
import com.sealstudios.spacex.ui.adapters.utils.LaunchClickListener

class LaunchesPagingAdapter(private val requestManager: RequestManager, private val launchClickListener: LaunchClickListener) :
    PagingDataAdapter<LaunchResponse, LaunchViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        return LaunchViewHolder(
            LaunchViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            requestManager,
            launchClickListener
        )
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<LaunchResponse>() {

            override fun areItemsTheSame(
                oldItem: LaunchResponse,
                newItem: LaunchResponse
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: LaunchResponse,
                newItem: LaunchResponse
            ): Boolean = oldItem.id == newItem.id
        }
    }

}

