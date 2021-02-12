package com.sealstudios.spacex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sealstudios.spacex.R
import com.sealstudios.spacex.databinding.FragmentCompanyBinding
import com.sealstudios.spacex.network.Status
import com.sealstudios.spacex.objects.CompanyResponse
import com.sealstudios.spacex.ui.viewmodels.CompanyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@AndroidEntryPoint
class CompanyFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentCompanyBinding? = null

    private val companyViewModel: CompanyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCompany()
        companyViewModel.retry()
    }

    private fun observeCompany() {
        companyViewModel.company.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        binding.onData(it.data)
                    } else {
                        binding.onError(getString(R.string.error),) { companyViewModel.retry() }
                    }
                }
                Status.ERROR -> binding.onError(
                    it.message ?: getString(R.string.error)
                ) { companyViewModel.retry() }
                Status.LOADING -> binding.onLoading()
            }
        })
    }

}

fun FragmentCompanyBinding.onData(companyResponse: CompanyResponse) {
    fun formatValuation(): String {
        val decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.US)
        return DecimalFormat("#,##0.#", decimalFormatSymbols).format(companyResponse.valuation)
    }

    fun formatCompanyDescription(): String {
        return "${companyResponse.name ?: "Company"} was founded by ${companyResponse.founder ?: "Unknown"} in ${companyResponse.founded ?: "Unknown"}. " +
                "It has now ${companyResponse.employees ?: "n/a"} employees, ${companyResponse.launch_sites ?: "n/a"} launch sites, " +
                "and is valued at USD ${formatValuation()}"
    }

    content.visibility = View.VISIBLE
    loading.visibility = View.GONE
    errorLayout.root.visibility = View.GONE
    description.text = formatCompanyDescription()

}

fun FragmentCompanyBinding.onError(errorMessage: String, retry: () -> Unit) {
    content.visibility = View.GONE
    loading.visibility = View.GONE
    errorLayout.root.visibility = View.VISIBLE
    errorLayout.errorText.text = errorMessage
    errorLayout.retryButton.setOnClickListener {
        retry()
    }
}

fun FragmentCompanyBinding.onLoading() {
    content.visibility = View.GONE
    loading.visibility = View.VISIBLE
    errorLayout.root.visibility = View.GONE
}
