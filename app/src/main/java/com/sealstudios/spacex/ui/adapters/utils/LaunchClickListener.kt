package com.sealstudios.spacex.ui.adapters.utils

import com.sealstudios.spacex.objects.LaunchResponse

interface LaunchClickListener {
    fun onItemSelected(launchResponse: LaunchResponse)
}