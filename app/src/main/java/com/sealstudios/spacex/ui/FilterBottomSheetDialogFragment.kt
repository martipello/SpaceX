package com.sealstudios.spacex.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.sealstudios.spacex.R
import com.sealstudios.spacex.databinding.FilterChipBinding
import com.sealstudios.spacex.databinding.FragmentFilterBinding
import com.sealstudios.spacex.extensions.getYearForDate
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addDateQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addSortOptionAscending
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addSortOptionDescending
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addSuccessQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getDefaultLaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getFiltersFromQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.isLaunchSuccessful
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.isSortOrderAscending
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.removeDateQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.removeSuccessQuery
import com.sealstudios.spacex.ui.viewmodels.FilterViewModel
import com.sealstudios.spacex.ui.viewmodels.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentFilterBinding? = null

    private val launchesViewModel: LaunchesViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val filterViewModel: FilterViewModel by hiltNavGraphViewModels(R.id.nav_graph)

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
        setUpFilterChips()
        observeQueryDataForFilterView()
    }

    private fun setUpFilterChips() {
        binding.filterChipGroup.removeAllViews()
        val allDateFilters = FilterViewModel.getAllDateFilters()
        for (index in allDateFilters.indices) {
            binding.filterChipGroup.addView(
                createChip(
                    allDateFilters[index],
                )
            )
        }
    }

    private fun observeQueryDataForFilterView() {
        filterViewModel.launchQueryData.observe(viewLifecycleOwner, {
            populateViewsForQueryData(it)
        })
    }

    private fun populateViewsForQueryData(launchQueryData: LaunchQueryData) {
        setUpButtons(launchQueryData)
        setLaunchSuccess(launchQueryData)
        setSortOptions(launchQueryData)
        setCheckedChip(launchQueryData)
        setFiltersCheckedChangeListener(launchQueryData)
    }

    private fun setUpButtons(launchQueryData: LaunchQueryData) {
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

    private fun setCheckedChip(launchQueryData: LaunchQueryData) {
        val allDateFilters = FilterViewModel.getAllDateFilters()
        for (index in allDateFilters.indices) {
            val isChecked =
                launchQueryData.getFiltersFromQuery()?.contains(allDateFilters[index])
            val chip: Chip = binding.filterChipGroup.getChildAt(index) as Chip
            chip.isChecked = isChecked == true
        }
    }

    private fun setFiltersCheckedChangeListener(launchQueryData: LaunchQueryData) {
        binding.filterChipGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != View.NO_ID) {
                val chip: Chip? = group.findViewById(checkedId)
                if (chip?.isChecked == true) {
                    launchQueryData.addDateQuery(
                        FilterViewModel.getDateQueryForFilter((chip.tag as String))
                    )
                    filterViewModel.setLaunchQueryData(launchQueryData)
                }
            } else {
                launchQueryData.removeDateQuery()
                filterViewModel.setLaunchQueryData(launchQueryData)
            }
        }
    }

    private fun createChip(filter: String): View {
        val chipBinding =
            FilterChipBinding.inflate(
                LayoutInflater.from(binding.filterChipGroup.context),
                binding.filterChipGroup,
                false
            )
        chipBinding.root.id = View.generateViewId()
        chipBinding.root.tag = filter
        chipBinding.root.text = filter.getYearForDate()
        chipBinding.root.isChecked = false

        return chipBinding.root
    }

    private fun setSortOptions(launchQueryData: LaunchQueryData) {
        binding.sortAscending.isChecked = launchQueryData.isSortOrderAscending()
        binding.sortDescending.isChecked = !launchQueryData.isSortOrderAscending()
        binding.sortGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.sort_ascending -> {
                    launchQueryData.addSortOptionAscending()
                }
                R.id.sort_descending -> {
                    launchQueryData.addSortOptionDescending()
                }
            }
            filterViewModel.setLaunchQueryData(launchQueryData)
        }
    }

    private fun setLaunchSuccess(launchQueryData: LaunchQueryData) {
        binding.onlySuccessfulLaunchesCheckBox.isChecked =
            launchQueryData.isLaunchSuccessful()
        binding.onlySuccessfulLaunchesCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                launchQueryData.addSuccessQuery(isChecked)
            } else {
                launchQueryData.removeSuccessQuery()
            }
            filterViewModel.setLaunchQueryData(
                launchQueryData
            )
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding.filterChipGroup.setOnCheckedChangeListener(null)
    }

    companion object {
        @JvmStatic
        fun newInstance(): FilterBottomSheetDialogFragment {
            return FilterBottomSheetDialogFragment()
        }

        const val getTag: String = "BottomSheetDialog"
    }
}