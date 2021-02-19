package com.sealstudios.spacex.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sealstudios.spacex.databinding.FilterChipBinding
import com.sealstudios.spacex.databinding.FragmentFilterBinding
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getAllDateFilters
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getDefaultLaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getFiltersFromQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getLaunchSuccessFromQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.isSortOrderAscending
import com.sealstudios.spacex.ui.viewmodels.FilterViewModel
import com.sealstudios.spacex.ui.viewmodels.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentFilterBinding? = null

    private val launchesViewModel: LaunchesViewModel by viewModels()
    private val filterViewModel: FilterViewModel by viewModels()

    private lateinit var launchQueryData: LaunchQueryData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        synchronizeQueryDataForFilterViewWithLaunchViewModel()
        observeQueryDataForFilterView()
        populateViews()
    }

    private fun synchronizeQueryDataForFilterViewWithLaunchViewModel(){
        Log.d("FILTER_FRAG", "synchronizeQueryDataForFilterViewWithLaunchViewModel")
        launchesViewModel.launchQueryData.observe(viewLifecycleOwner, Observer {
            Log.d("FILTER_FRAG", "setLaunchQueryData")
            filterViewModel.setLaunchQueryData(it)
        })
    }

    private fun observeQueryDataForFilterView() {
        Log.d("FILTER_FRAG", "observeQueryDataForFilterView")
        filterViewModel.launchQueryData.observe(viewLifecycleOwner, Observer {
            Log.d("FILTER_FRAG", "populateViewsWithQueryData observer")
            launchQueryData = it
            populateViewsWithQueryData(it)
        })
    }

    private fun populateViews() {
        binding.closeFilterDialogButton.setOnClickListener {
            this.dismiss()
        }
        binding.clearFiltersButton.setOnClickListener {
            filterViewModel.setLaunchQueryData(getDefaultLaunchQueryData())
        }
        binding.applyFiltersButton.setOnClickListener {
            launchesViewModel.setLaunchQueryData(launchQueryData)
            this.dismiss()
        }
    }

    private fun populateViewsWithQueryData(launchQueryData: LaunchQueryData){
        Log.d("FILTER_FRAG", "populateViewsWithQueryData $launchQueryData")

        binding.onlySuccessfulLaunchesCheckBox.isChecked = getLaunchSuccessFromQuery(launchQueryData)

        binding.sortAscending.isChecked = isSortOrderAscending(launchQueryData)
        binding.sortDescending.isChecked = !isSortOrderAscending(launchQueryData)

        binding.onlySuccessfulLaunchesCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            // set success launches for query data
        }

        binding.sortGroup.setOnCheckedChangeListener { group, checkedId ->
            //set sort order for query data
        }

        val allFilters = getFiltersFromQuery(getAllDateFilters())
        Log.d("FILTER_FRAG", "populateViewsWithQueryData ${getAllDateFilters()}")
        val selectedFilters = launchQueryData.query?.let { getFiltersFromQuery(it) } ?: listOf()

        for (filter in allFilters){
            Log.d("FILTER_FRAG", "filter $filter")
            binding.filterChipGroup.addView(createChip(filter, binding.filterChipGroup))
        }
    }

    private fun createChip(filter: String, viewGroup: ViewGroup): View{
        val chipBinding = FilterChipBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        chipBinding.root.text = filter
        chipBinding.root.isChecked = true
        chipBinding.root.setOnCheckedChangeListener { buttonView, isChecked ->
            // remove/add from filters
        }
        return chipBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): FilterBottomSheetDialogFragment {
            val fragment = FilterBottomSheetDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}