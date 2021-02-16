package com.sealstudios.spacex.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sealstudios.spacex.databinding.FilterDialogLayoutBinding
import com.sealstudios.spacex.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterFab.setOnClickListener {
            openFilterDialog(it.context)
        }
    }

    private fun openFilterDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val dialogLayout = FilterDialogLayoutBinding.inflate(layoutInflater)
        builder.setPositiveButton("OK", null)
        builder.setView(dialogLayout.root)
        builder.show()
    }
}