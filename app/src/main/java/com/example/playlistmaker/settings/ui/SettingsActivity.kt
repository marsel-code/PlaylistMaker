package com.example.playlistmaker.settings.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.SettingsActivityBinding
import com.example.playlistmaker.settings.presentation.state.SettingsState
import com.example.playlistmaker.settings.presentation.view_model.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding

    private val viewModel by viewModel <SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

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