package com.sealstudios.spacex.ui

import LaunchesLoadStateAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.bumptech.glide.RequestManager
import com.sealstudios.spacex.R
import com.sealstudios.spacex.databinding.FragmentLaunchesBinding
import com.sealstudios.spacex.ui.adapters.LaunchesPagingAdapter
import com.sealstudios.spacex.ui.adapters.utils.LaunchesListDividerDecoration
import com.sealstudios.spacex.ui.viewmodels.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LaunchesFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentLaunchesBinding? = null

    private val launchesViewModel: LaunchesViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private lateinit var launchesPagingAdapter: LaunchesPagingAdapter

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaunchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchesPagingAdapter = LaunchesPagingAdapter(glide)
        addLoadingStateListener()
        setUpRecyclerView()
        observeLaunches()
    }

    private fun observeLaunches() {
        launchesViewModel.launches.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                launchesPagingAdapter.submitData(it)
            }
        })
    }

    private fun setUpRecyclerView() = binding.launchesRecyclerView.apply {
        adapter = launchesPagingAdapter.withLoadStateFooter(footer = LaunchesLoadStateAdapter { launchesPagingAdapter.retry() })
        addItemDecoration(
            LaunchesListDividerDecoration(
                R.drawable.divider,
                context,
                context.resources.getDimensionPixelSize(R.dimen.small_margin_8dp)
            )
        )
    }

    private fun addLoadingStateListener() {
        launchesPagingAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {
                    binding.onLoading()
                }
                is LoadState.Error -> {
                    binding.onError((it.refresh as LoadState.Error).error.localizedMessage)
                }
                else -> {
                    if (launchesPagingAdapter.itemCount == 0){
                        binding.onDataEmpty()
                    } else {
                        binding.onData()
                    }
                }
            }
        }
    }

    private fun FragmentLaunchesBinding.onDataEmpty() {
        content.visibility = View.VISIBLE
        loading.visibility = View.GONE
        errorLayout.root.visibility = View.GONE
        binding.emptyText.visibility = View.VISIBLE
    }

    private fun FragmentLaunchesBinding.onData() {
        content.visibility = View.VISIBLE
        binding.emptyText.visibility = View.GONE
        loading.visibility = View.GONE
        errorLayout.root.visibility = View.GONE
    }

    private fun FragmentLaunchesBinding.onLoading() {
        content.visibility = View.GONE
        loading.visibility = View.VISIBLE
        errorLayout.root.visibility = View.GONE
    }

    private fun FragmentLaunchesBinding.onError(errorMessage: String?) {
        content.visibility = View.GONE
        loading.visibility = View.GONE
        errorLayout.root.visibility = View.VISIBLE
        errorMessage?.let{
            errorLayout.errorText.text = it
        }
        errorLayout.retryButton.setOnClickListener {
            launchesPagingAdapter.refresh()
        }
    }
}