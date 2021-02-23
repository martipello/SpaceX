package com.sealstudios.spacex.ui.viewmodels

import androidx.lifecycle.*
import com.sealstudios.spacex.network.Resource
import com.sealstudios.spacex.network.SpaceXService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val spaceXService: SpaceXService
) : ViewModel() {

    private val retry: MutableLiveData<Boolean> = MutableLiveData()

    val company = retry.switchMap {
        liveData {
            emit(Resource.loading(null))
            emit(spaceXService.getCompanyResponse())
        }
    }

    fun retry() {
        retry.value = true
    }

}