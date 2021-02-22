package com.sealstudios.spacex.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sealstudios.spacex.R
import com.sealstudios.spacex.databinding.LaunchViewHolderBinding
import com.sealstudios.spacex.objects.LaunchResponse
import com.sealstudios.spacex.objects.LaunchResponse.Companion.daysFromLaunch
import com.sealstudios.spacex.objects.LaunchResponse.Companion.launchTenseLabelText
import com.sealstudios.spacex.objects.LaunchResponse.Companion.missionDateText
import com.sealstudios.spacex.ui.adapters.utils.LaunchClickListener

class LaunchViewHolder(
    private val launchViewHolderBinding: LaunchViewHolderBinding,
    private val requestManager: RequestManager,
    private val launchClickListener: LaunchClickListener
) : RecyclerView.ViewHolder(launchViewHolderBinding.root) {

    fun bind(launchResponse: LaunchResponse) {
        with(launchViewHolderBinding) {
            setDaysFromOrSinceNow(launchResponse)
            this.missionNameText.text = launchResponse.name
            this.missionDateText.text = launchResponse.missionDateText()

            this.successfulLaunchCheckBox.isChecked = launchResponse.success ?: false
            this.missionRocketText.text = launchResponse.rocket?.name ?: "Unknown"
            this.missionRocketTypeText.text = launchResponse.rocket?.type ?: "Unknown"
            setImage(launchResponse.links?.patch?.small)
            this.background.setOnClickListener {
                launchClickListener.onItemSelected(launchResponse)
            }
        }
    }

    private fun setDaysFromOrSinceNow(launchResponse: LaunchResponse) {
        if (launchResponse.launchTenseLabelText().isNullOrEmpty()) {
            launchViewHolderBinding.missionDaysSinceFromNowLabel.visibility = View.GONE
            launchViewHolderBinding.missionDaysSinceFromNowText.visibility = View.GONE
        } else {
            launchViewHolderBinding.missionDaysSinceFromNowLabel.text =
                launchResponse.launchTenseLabelText()
            launchViewHolderBinding.missionDaysSinceFromNowText.text =
                launchResponse.daysFromLaunch()
        }
    }

    private fun setImage(url: String?) {
        requestManager.load(url)
            .placeholder(R.drawable.ic_space_x_logo)
            .into(launchViewHolderBinding.launchImage)
    }

}
