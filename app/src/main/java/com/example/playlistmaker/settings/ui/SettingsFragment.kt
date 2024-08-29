package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.presentation.state.SettingsState
import com.example.playlistmaker.settings.presentation.view_model.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModel<SettingViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val shareButton = binding.share
        shareButton.setOnClickListener {
            viewModel.shareButton()
        }

        val supportButton = binding.support
        supportButton.setOnClickListener {
            viewModel.supportButton()
        }

        val agreementUserButton = binding.agreementUser
        agreementUserButton.setOnClickListener {
            viewModel.agreementUserButton()
        }

        val themeSwitcher = binding.themeSwitcher
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.checkedChangeListener(checked)
        }

        viewModel.getSate().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is SettingsState.StatusObserver -> themeSwitcher.isChecked = screenState.switch
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
