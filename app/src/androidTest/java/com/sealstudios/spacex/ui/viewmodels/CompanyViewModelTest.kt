package com.sealstudios.spacex.ui.viewmodels

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sealstudios.spacex.network.Resource
import com.sealstudios.spacex.network.Status
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.testUtils.getValueBlocking
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CompanyViewModelTest {

    @MockK
    private lateinit var spaceXService: SpaceXService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getCompany() {
        runBlocking {
            coEvery { spaceXService.getCompanyResponse() } returns Resource.loading(null)
            val companyViewModel = CompanyViewModel(spaceXService = spaceXService)
            withContext(Dispatchers.Main) {
                companyViewModel.retry()
            }
            val companyResource = companyViewModel.company.getValueBlocking()
            assertThat(companyResource?.status, equalTo(Status.LOADING))
        }
    }

    @Test
    fun retry() {
    }
}

