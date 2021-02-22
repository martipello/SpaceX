package com.sealstudios.spacex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sealstudios.spacex.R
import com.sealstudios.spacex.databinding.WebPageSelectionsBottomSheetDialogFragmentBinding
import com.sealstudios.spacex.extensions.asUri
import com.sealstudios.spacex.extensions.openInBrowser
import com.sealstudios.spacex.objects.LaunchLinks
import com.sealstudios.spacex.ui.viewmodels.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WebPageSelectionsBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val binding get() = _binding!!
    private var _binding: WebPageSelectionsBottomSheetDialogFragmentBinding? = null

    private val launchesViewModel: LaunchesViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            WebPageSelectionsBottomSheetDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSelectedLaunch()
    }

    private fun observeSelectedLaunch() {
        launchesViewModel.selectedLaunchLinks.observe(viewLifecycleOwner, {
            populateViews(it)
        })
    }

    private fun populateViews(launchLinks: LaunchLinks) {
        setWikipediaView(launchLinks)
        setArticleView(launchLinks)
        setWebCastView(launchLinks)
        binding.closeDialogButton.setOnClickListener {
            this.dismiss()
        }
    }

    private fun setWebCastView(launchLinks: LaunchLinks) {
        if (launchLinks.webcast.isNullOrEmpty()) {
            binding.videoPageHolder.visibility = View.GONE
        } else {
            binding.videoPageHolder.setOnClickListener {
                launchLinks.webcast.asUri().openInBrowser(binding.root.context)
            }
        }
    }

    private fun setArticleView(launchLinks: LaunchLinks) {
        if (launchLinks.article.isNullOrEmpty()) {
            binding.articleHolder.visibility = View.GONE
        } else {
            binding.articleHolder.setOnClickListener {
                launchLinks.article.asUri().openInBrowser(binding.root.context)
            }
        }
    }

    private fun setWikipediaView(launchLinks: LaunchLinks) {
        if (launchLinks.wikipedia.isNullOrEmpty()) {
            binding.wikipediaHolder.visibility = View.GONE
        } else {
            binding.wikipediaHolder.setOnClickListener {
                launchLinks.wikipedia.asUri().openInBrowser(binding.root.context)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): WebPageSelectionsBottomSheetDialogFragment {
            return WebPageSelectionsBottomSheetDialogFragment()
        }
        const val getTag: String = "OpenWebPageBottomSheetDialog"
    }
}