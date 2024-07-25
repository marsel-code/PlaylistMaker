package com.example.playlistmaker.settings.ui


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.databinding.SettingsActivityBinding
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.view_model.PlayerViewModel
import com.example.playlistmaker.search.presentation.view_model.SearchViewModel
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.settings.presentation.state.SettingsState
import com.example.playlistmaker.settings.presentation.view_model.SettingViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding
    private lateinit var viewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingViewModel.getViewModelFactory()
        )[SettingViewModel::class.java]

        val backButton = binding.backMain
        backButton.setOnClickListener {
            finish()
        }

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

        viewModel.getSate().observe(this) { screenState ->
            when (screenState) {
                is SettingsState.StatusObserver -> themeSwitcher.isChecked = screenState.switch
            }
        }
    }
}