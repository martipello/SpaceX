package com.sealstudios.spacex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sealstudios.spacex.databinding.FragmentLaunchesBinding
import com.sealstudios.spacex.databinding.FragmentMainBinding
import com.sealstudios.spacex.ui.viewmodels.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchesFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentLaunchesBinding? = null

    private val launchesViewModel: LaunchesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLaunches()
    }

    private fun observeLaunches(){

    }
}