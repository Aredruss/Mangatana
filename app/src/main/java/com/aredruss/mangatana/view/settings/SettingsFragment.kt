package com.aredruss.mangatana.view.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.data.datastore.AppState
import com.aredruss.mangatana.databinding.FragmentSettingsBinding
import com.aredruss.mangatana.utils.CLEARED_ALL_DATA
import com.aredruss.mangatana.view.extensions.changeTheme
import com.aredruss.mangatana.view.extensions.context
import com.aredruss.mangatana.view.extensions.showSingle
import com.aredruss.mangatana.view.util.BaseFragment
import com.aredruss.mangatana.view.util.DialogHelper
import com.aredruss.mangatana.view.util.dialog.Choices
import com.aredruss.mangatana.view.util.dialog.SettingDialog
import com.aredruss.mangatana.view.util.dialog.SettingDialog.Companion.SETTING_DIALOG_TAG
import com.aredruss.mangatana.view.util.dialog.THEME
import com.github.terrakok.modo.back
import com.microsoft.appcenter.analytics.Analytics
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModel()
    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeSettings()
    }

    override fun setupViews(): Unit = with(binding) {
        backBtn.setOnClickListener {
            modo.back()
        }
        setupClearBtn()
    }

    private fun observeSettings() {
        lifecycleScope.launchWhenCreated {
            viewModel.getSettings()
            viewModel.settingState.collect { state ->
                when (state) {
                    is SettingsState.Success -> {
                        setupThemeBtn(state.appState.darkModeState)
                        setupFilterBtn(state.appState.allowNsfw)
                    }
                    is SettingsState.Empty -> {
                    }
                }
            }
        }
    }

    private fun setupThemeBtn(themeCode: Int) = with(binding) {
        themePv.setSubText(when(themeCode) {
            AppState.DARK_MODE_DISABLED -> {
                getString(R.string.settings_color_vanilla)
            }
            AppState.DARK_MODE_ENABLED -> {
                getString(R.string.settings_color_chocolate)
            }
            else -> {
                getString(R.string.settings_color_auto)
            }
        })

        themePv.setOnClickListener {
            showChoice(themeCode, THEME.values(), this@SettingsFragment::changeTheme)
        }
    }

    private fun setupFilterBtn(isChecked: Boolean) = with(binding) {
        val filterSwitch = SwitchCompat(context())
        filterSwitch.isChecked = isChecked
        filterSwitch.setOnCheckedChangeListener { _, isChecked ->
            changeFilter(isChecked)
        }
        filterPv.setView(filterSwitch)
    }

    private fun setupClearBtn() = with(binding) {
        deletePv.setOnClickListener {
            DialogHelper.buildConfirmDialog(
                context = this.context(),
                title = R.string.dialog_clear,
                message = R.string.dialog_clear_message,
                argument = Unit,
                action = this@SettingsFragment::clearDatabase
            )
        }
    }

    private fun changeTheme(themeCode: Int) {
        activity?.changeTheme(themeCode)
        viewModel.updateTheme(themeCode)
    }

    private fun changeFilter(isOn: Boolean) {
        viewModel.updateFilter(isOn)
    }

    private fun <T : Choices> showChoice(current: Int, choices: Array<T>, action: (Int) -> Unit) {
        SettingDialog(current, choices, action).showSingle(childFragmentManager, SETTING_DIALOG_TAG)
    }

    @Suppress("UnusedPrivateMember")
    private fun clearDatabase(unit: Unit) {
        Analytics.trackEvent(CLEARED_ALL_DATA)
        viewModel.clearDatabase()
    }

    companion object {
        fun create() = SettingsFragment()

        private const val DARK_THEME = 0
        private const val LIGHT_THEME = 1
    }
}
