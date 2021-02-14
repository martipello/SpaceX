package com.sealstudios.spacex.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sealstudios.spacex.R
import com.sealstudios.spacex.databinding.LaunchViewHolderBinding
import com.sealstudios.spacex.objects.LaunchResponse
import com.sealstudios.spacex.objects.LaunchTense

class LaunchViewHolder(
    private val launchViewHolderBinding: LaunchViewHolderBinding,
    private val requestManager: RequestManager
) : RecyclerView.ViewHolder(launchViewHolderBinding.root) {

    fun bind(launchResponse: LaunchResponse) {
        with(launchViewHolderBinding) {
            setDaysFromOrSinceNow(launchResponse.date_utc, launchResponse.tdb)
            this.missionNameText.text = launchResponse.name
            this.missionDateText.text =
                if (launchResponse.tdb == true) "TBD" else LaunchResponse.formatDate(launchResponse.date_utc)
            this.successfulLaunchCheckBox.isChecked = launchResponse.success ?: false
            this.missionRocketText.text = "${launchResponse.rocket}"
            setImage(launchResponse.links?.patch?.small)
        }
    }

    private fun setDaysFromOrSinceNow(dateUtc: String?, launchTBD: Boolean?) {
        launchViewHolderBinding.missionDaysSinceFromNowText.text =
            if (launchTBD == true) "TBD" else LaunchResponse.getDaysSinceFromMissionLaunchText(dateUtc)

        when (LaunchResponse.isLaunchInTheFuture(dateUtc)) {
            LaunchTense.NOW -> {
                launchViewHolderBinding.missionDaysSinceFromNowLabel.text =
                    launchViewHolderBinding.root.context.getString(
                        R.string.mission_days_since_from_now_label,
                        "from"
                    )
            }
            LaunchTense.FUTURE -> {
                launchViewHolderBinding.missionDaysSinceFromNowLabel.text =
                    launchViewHolderBinding.root.context.getString(
                        R.string.mission_days_since_from_now_label,
                        "from"
                    )
            }
            LaunchTense.PAST -> {
                launchViewHolderBinding.missionDaysSinceFromNowLabel.text =
                    launchViewHolderBinding.root.context.getString(
                        R.string.mission_days_since_from_now_label,
                        "since"
                    )
            }
            LaunchTense.UNKNOWN -> {
                launchViewHolderBinding.missionDaysSinceFromNowLabel.visibility = View.GONE
                launchViewHolderBinding.missionDaysSinceFromNowText.visibility = View.GONE
            }
        }

    }

    private fun setImage(url: String?) {
        requestManager.load(url)
            .placeholder(R.drawable.ic_space_x_logo)
            .into(launchViewHolderBinding.launchImage)
    }

}
