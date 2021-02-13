package com.sealstudios.spacex.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.sealstudios.spacex.databinding.FragmentLaunchesBinding
import com.sealstudios.spacex.ui.adapters.LaunchesPagingAdapter
import com.sealstudios.spacex.ui.viewmodels.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LaunchesFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentLaunchesBinding? = null

    private val launchesViewModel: LaunchesViewModel by viewModels()
    private lateinit var launchesPagingAdapter: LaunchesPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchesPagingAdapter = LaunchesPagingAdapter()
        setUpRecyclerView()
        observeLaunches()
    }

    private fun observeLaunches() {
        launchesViewModel.launches.observe(viewLifecycleOwner, Observer {
            Log.d("PAGING", "observeLaunches Observer fired")
            lifecycleScope.launch {
                launchesPagingAdapter.submitData(it)
                Log.d("PAGING", "data count ${launchesPagingAdapter.itemCount}")
            }
        })
    }

    private fun setUpRecyclerView() = binding.launchesRecyclerView.apply {
        adapter = launchesPagingAdapter
    }
}