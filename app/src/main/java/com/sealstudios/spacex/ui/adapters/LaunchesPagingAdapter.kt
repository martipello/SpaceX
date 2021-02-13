package com.sealstudios.spacex.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.sealstudios.spacex.databinding.LaunchViewHolderBinding
import com.sealstudios.spacex.objects.LaunchesResponse

class LaunchesPagingAdapter(private val requestManager: RequestManager) :
    PagingDataAdapter<LaunchesResponse, LaunchViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {

        return LaunchViewHolder(
            LaunchViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            requestManager
        )
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<LaunchesResponse>() {

            override fun areItemsTheSame(
                oldItem: LaunchesResponse,
                newItem: LaunchesResponse
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: LaunchesResponse,
                newItem: LaunchesResponse
            ): Boolean = oldItem.id == newItem.id
        }
    }

}

