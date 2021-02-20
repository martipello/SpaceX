package com.sealstudios.spacex.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.sealstudios.spacex.R
import com.sealstudios.spacex.databinding.FilterChipBinding
import com.sealstudios.spacex.databinding.FragmentFilterBinding
import com.sealstudios.spacex.extensions.getYearForDate
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addSortOption
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getDefaultLaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getFiltersFromQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.isLaunchSuccessful
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.isSortOrderAscending
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.removeQuery
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2016
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2017
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2018
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2019
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2020
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2021
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
    private val selectedYear = sortedSetOf<String>()

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
        setInitialQueryData()
        observeQueryDataForFilterView()
        populateViews()
    }

    private fun setInitialQueryData() {
        launchesViewModelObserver = Observer<LaunchQueryData> {
            setInitialSelectedYears(it)
            populateViewsInitialValues(it)
            filterViewModel.setLaunchQueryData(it)
        }
        launchesViewModel.launchQueryData.observe(viewLifecycleOwner, launchesViewModelObserver)
    }

    private fun setInitialSelectedYears(launchQueryData: LaunchQueryData) {
        selectedYear.clear()
        launchQueryData.query?.let {
            val filtersFromQuery = launchQueryData.getFiltersFromQuery()
            if (filtersFromQuery.isNotEmpty()) {
                selectedYear.add(
                    filtersFromQuery.first()
                )
            }
        }
    }

    private fun populateViewsInitialValues(launchQueryData: LaunchQueryData) {
        binding.onlySuccessfulLaunchesCheckBox.isChecked =
            launchQueryData.isLaunchSuccessful()
        binding.sortAscending.isChecked = launchQueryData.isSortOrderAscending()
        binding.sortDescending.isChecked = !launchQueryData.isSortOrderAscending()

        binding.filterChipGroup.removeAllViews()
        for (index in getAllDateFilters().indices) {
            val isSelected = selectedYear.contains(getAllDateFilters()[index])
            binding.filterChipGroup.addView(
                createChip(
                    getAllDateFilters()[index],
                    isSelected
                )
            )
        }
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
        setLaunchSuccessCheckedChangeListener(launchQueryData)
        setSortOptionCheckedChangeListener(launchQueryData)
        setFiltersCheckedChangeListener()
    }

    private fun setFiltersCheckedChangeListener() {
        binding.filterChipGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == View.NO_ID) {
                selectedYear.clear()
            } else {
                val chip: Chip = group.findViewById(checkedId)
                if (chip.isChecked) {
                    selectedYear.clear()
                    for (year in getDateQueryForFilter((chip.tag as String))) {
                        selectedYear.add(year)
                    }
                }
            }
            setSelectedYearToLaunchQueryData()
        }
    }

    private fun setSortOptionCheckedChangeListener(launchQueryData: LaunchQueryData) {
        binding.sortGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.sort_ascending -> {
                    launchQueryData.addSortOption(
                        "date_utc",
                        "asc"
                    )
                }
                R.id.sort_descending -> {
                    launchQueryData.addSortOption(
                        "date_utc",
                        "desc"
                    )
                }
            }
            filterViewModel.setLaunchQueryData(launchQueryData)
        }
    }

    private fun setLaunchSuccessCheckedChangeListener(launchQueryData: LaunchQueryData) {
        binding.onlySuccessfulLaunchesCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                launchQueryData.addQuery(
                    "success",
                    isChecked
                )
            } else {
                launchQueryData.removeQuery(
                    "success"
                )
            }
            filterViewModel.setLaunchQueryData(
                launchQueryData
            )
        }
    }

    private fun createChip(filter: String, isSelected: Boolean): View {
        val chipBinding =
            FilterChipBinding.inflate(
                LayoutInflater.from(binding.filterChipGroup.context),
                binding.filterChipGroup,
                false
            )
        chipBinding.root.id = View.generateViewId()
        chipBinding.root.tag = filter
        chipBinding.root.text = filter.getYearForDate()
        chipBinding.root.isChecked = isSelected

        return chipBinding.root
    }

    private fun setSelectedYearToLaunchQueryData() {
        if (selectedYear.isNotEmpty()) {
            launchQueryData.addQuery(
                "date_utc",
                dateQuery(selectedYear.first(), selectedYear.last())
            )
        } else {
            launchQueryData.removeQuery("date_utc")
        }
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
        fun newInstance(): FilterBottomSheetDialogFragment {
            return FilterBottomSheetDialogFragment()
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

        private fun getDateQueryForFilter(filter: String): List<String> {
            when (filter) {
                "2016-01-01T00:00:00.000Z" -> return dateQuery2016().values.toList()
                "2017-01-01T00:00:00.000Z" -> return dateQuery2017().values.toList()
                "2018-01-01T00:00:00.000Z" -> return dateQuery2018().values.toList()
                "2019-01-01T00:00:00.000Z" -> return dateQuery2019().values.toList()
                "2020-01-01T00:00:00.000Z" -> return dateQuery2020().values.toList()
                "2021-01-01T00:00:00.000Z" -> return dateQuery2021().values.toList()
            }
            return listOf()
        }

        const val getTag: String = "BottomSheetDialog"
    }
}