package com.sealstudios.spacex.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sealstudios.spacex.repositories.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    val spaceXRepository: SpaceXRepository,
    private val savedStateHandle: SavedStateHandle, ) : ViewModel() {

}