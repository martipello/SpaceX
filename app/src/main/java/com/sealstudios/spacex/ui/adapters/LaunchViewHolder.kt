package com.sealstudios.spacex.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sealstudios.spacex.R
import com.sealstudios.spacex.databinding.LaunchViewHolderBinding
import com.sealstudios.spacex.objects.LaunchesResponse

class LaunchViewHolder(
    private val launchViewHolderBinding: LaunchViewHolderBinding,
    private val requestManager: RequestManager
) : RecyclerView.ViewHolder(launchViewHolderBinding.root) {

    fun bind(launchesResponse: LaunchesResponse) {
        with(launchViewHolderBinding) {
            this.missionNameText.text = launchesResponse.name
            this.successfulLaunchCheckBox.isChecked = launchesResponse.success ?: false
            this.missionRocketText.text = launchesResponse.rocket
            setImage(launchesResponse.links?.patch?.small)
        }
    }

    private fun setImage(url: String?) {
        requestManager.load(url)
            .placeholder(R.mipmap.ic_launcher_round)
            .circleCrop()
            .into(launchViewHolderBinding.launchImage)
    }

}