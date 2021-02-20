package com.sealstudios.spacex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sealstudios.spacex.databinding.WebPageSelectionsBottomSheetDialogFragmentBinding
import com.sealstudios.spacex.extensions.asUri
import com.sealstudios.spacex.extensions.openInBrowser
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WebPageSelectionsBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val binding get() = _binding!!
    private var _binding: WebPageSelectionsBottomSheetDialogFragmentBinding? = null


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
        populateViews()
    }

    private fun populateViews() {
        if (this.arguments?.getString(WIKIPEDIA).isNullOrEmpty()) {
            binding.wikipediaHolder.visibility = View.GONE
        } else {
            binding.wikipediaHolder.setOnClickListener {
                this.arguments?.getString(WIKIPEDIA).asUri().openInBrowser(binding.root.context)
            }
        }
        if (this.arguments?.getString(ARTICLE).isNullOrEmpty()) {
            binding.articleHolder.visibility = View.GONE
        } else {
            binding.articleHolder.setOnClickListener {
                this.arguments?.getString(ARTICLE).asUri().openInBrowser(binding.root.context)
            }
        }
        if (this.arguments?.getString(WEB_CAST).isNullOrEmpty()) {
            binding.videoPageHolder.visibility = View.GONE
        } else {
            binding.videoPageHolder.setOnClickListener {
                this.arguments?.getString(WEB_CAST).asUri().openInBrowser(binding.root.context)
            }
        }
        binding.closeDialogButton.setOnClickListener {
            this.dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): WebPageSelectionsBottomSheetDialogFragment {
            val fragment = WebPageSelectionsBottomSheetDialogFragment()
            fragment.arguments = bundle
            return fragment
        }

        const val getTag: String = "OpenWebPageBottomSheetDialog"
        const val WIKIPEDIA = "wikipedia"
        const val WEB_CAST = "webcast"
        const val ARTICLE = "article"
    }
}