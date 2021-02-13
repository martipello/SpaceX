package com.sealstudios.spacex.ui.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.sealstudios.spacex.databinding.LaunchViewHolderBinding
import com.sealstudios.spacex.objects.LaunchesResponse

class LaunchViewHolder(
    private val launchViewHolderBinding: LaunchViewHolderBinding
) : RecyclerView.ViewHolder(launchViewHolderBinding.root) {

    fun bind(launchesResponse: LaunchesResponse){
        Log.d("PAGING", "bind view holder $launchesResponse")
        with(launchViewHolderBinding){
            this.missionNameText.text = launchesResponse.name
        }
    }

}