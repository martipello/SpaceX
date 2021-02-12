package com.sealstudios.spacex.ui.viewmodels

import androidx.lifecycle.*
import com.sealstudios.spacex.network.Resource
import com.sealstudios.spacex.repositories.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val spaceXRepository: SpaceXRepository
) : ViewModel() {

    private val retry: MutableLiveData<Boolean> = MutableLiveData()

    val company = retry.switchMap {
        liveData {
            emit(Resource.loading(null))
            emit(spaceXRepository.getCompanyResponse())
        }
    }

    fun retry() {
        retry.value = true
    }

}