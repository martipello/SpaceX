package com.sealstudios.spacex.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sealstudios.spacex.R
import com.sealstudios.spacex.databinding.FilterChipBinding
import com.sealstudios.spacex.databinding.FragmentFilterBinding
import com.sealstudios.spacex.extensions.getYearForDate
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addSortOption
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getDefaultLaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getFiltersFromQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getLaunchSuccessFromQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.isSortOrderAscending
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery
import com.sealstudios.spacex.ui.viewmodels.FilterViewModel
import com.sealstudios.spacex.ui.viewmodels.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentFilterBinding? = null

    private val launchesViewModel: LaunchesViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val filterViewModel: FilterViewModel by viewModels()
    private lateinit var launchesViewModelObserver: Observer<LaunchQueryData>
    private lateinit var filtersViewModelObserver: Observer<LaunchQueryData>

    private lateinit var launchQueryData: LaunchQueryData
    private val selectedFilters = sortedSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        synchronizeQueryDataForFilterViewWithLaunchViewModel()
        observeQueryDataForFilterView()
        populateViews()
    }

    private fun synchronizeQueryDataForFilterViewWithLaunchViewModel() {
        launchesViewModelObserver = Observer<LaunchQueryData> {
            populateViewsInitialValues(it)
            filterViewModel.setLaunchQueryData(it)
        }
        launchesViewModel.launchQueryData.observe(viewLifecycleOwner, launchesViewModelObserver)
    }

    private fun populateViewsInitialValues(launchQueryData: LaunchQueryData) {
        Log.d("FILTER_FRAG", "populateViewsInitialValues $launchQueryData")
        binding.onlySuccessfulLaunchesCheckBox.isChecked =
            getLaunchSuccessFromQuery(launchQueryData)
        binding.sortAscending.isChecked = isSortOrderAscending(launchQueryData)
        binding.sortDescending.isChecked = !isSortOrderAscending(launchQueryData)
    }

    private fun observeQueryDataForFilterView() {
        filtersViewModelObserver = Observer<LaunchQueryData> {
            launchQueryData = it
            populateViewsWithQueryData(it)
        }
        filterViewModel.launchQueryData.observe(viewLifecycleOwner, filtersViewModelObserver)
    }

    private fun populateViews() {
        binding.closeFilterDialogButton.setOnClickListener {
            this.dismiss()
        }
        binding.clearFiltersButton.setOnClickListener {
            this.dismiss()
            launchesViewModel.setLaunchQueryData(getDefaultLaunchQueryData())
        }
        binding.applyFiltersButton.setOnClickListener {
            this.dismiss()
            launchesViewModel.setLaunchQueryData(launchQueryData)
        }
    }

    private fun populateViewsWithQueryData(launchQueryData: LaunchQueryData) {
        binding.onlySuccessfulLaunchesCheckBox.setOnCheckedChangeListener { _, isChecked ->
            filterViewModel.setLaunchQueryData(
                launchQueryData.addQuery(
                    "success",
                    isChecked
                )
            )
        }

        binding.sortGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.sort_ascending -> {
                    filterViewModel.setLaunchQueryData(
                        launchQueryData.addSortOption(
                            "date_utc",
                            "asc"
                        )
                    )
                }
                R.id.sort_descending -> {
                    filterViewModel.setLaunchQueryData(
                        launchQueryData.addSortOption(
                            "date_utc",
                            "desc"
                        )
                    )
                }
            }
        }

        val allFilters = getAllDateFilters()
//        val selectedFromAndToDates = launchQueryData.query?.let { getFiltersFromQuery(it) } ?: listOf()

        binding.filterChipGroup.removeAllViews()
        for (index in allFilters.indices) {
            binding.filterChipGroup.addView(
                createChip(
                    allFilters[index],
                    index,
                    binding.filterChipGroup
                )
            )
        }
    }

    private fun getAllDateFilters(): List<String> {
        return mutableListOf(
            "2016-01-01T00:00:00.000Z",
            "2017-01-01T00:00:00.000Z",
            "2018-01-01T00:00:00.000Z",
            "2019-01-01T00:00:00.000Z",
            "2020-01-01T00:00:00.000Z",
            "2021-01-01T00:00:00.000Z"
        )
    }

    private fun createChip(filter: String, index: Int, viewGroup: ViewGroup): View {
        val chipBinding =
            FilterChipBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        chipBinding.root.text = filter.getYearForDate()
        chipBinding.root.tag = filter
//        chipBinding.root.isChecked = true
        chipBinding.root.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                selectedFilters.add(getAllDateFilters()[index])
            } else {
                selectedFilters.remove(getAllDateFilters()[index])
            }
            launchQueryData.addQuery("date_utc", dateQuery(selectedFilters.first(), selectedFilters.last()))
        }
        return chipBinding.root
    }

    private fun removeObservers() {
        filterViewModel.launchQueryData.removeObserver(filtersViewModelObserver)
        launchesViewModel.launchQueryData.removeObserver(launchesViewModelObserver)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        removeObservers()
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