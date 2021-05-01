package com.aredruss.mangatana.view.home

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.FragmentHomeBinding
import com.aredruss.mangatana.modo.ScreenCategory
import com.aredruss.mangatana.modo.Screens
import com.aredruss.mangatana.view.util.BaseFragment
import com.github.terrakok.modo.forward

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun setupViews() {
        setupButtons()
        setupMenu()
    }

    private fun setupButtons() = with(binding) {
        homeMenuIb.setOnClickListener {
        }
        inProgressBtn.setOnClickListener {
            openMediaFragment(ScreenCategory.ON_GOING)
        }
        upNextBtn.setOnClickListener {
            openMediaFragment(ScreenCategory.BACKLOG)
        }
        finishedBtn.setOnClickListener {
            openMediaFragment(ScreenCategory.FINISHED)
        }
        starredBtn.setOnClickListener {
            openMediaFragment(ScreenCategory.STARRED)
        }
        exploreBtn.setOnClickListener {
            openMediaFragment(ScreenCategory.EXPLORE)
        }
    }

    private fun setupMenu() = with(binding) {
        homeMenuIb.setOnClickListener {
            val menu = PopupMenu(this.root.context, it).apply {
                inflate(R.menu.menu_main)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_about -> modo.forward(Screens.About())
                        R.id.action_settings -> modo.forward(Screens.Settings())
                    }
                    return@setOnMenuItemClickListener false
                }
                show()
            }
        }
    }

    private fun openMediaFragment(type: Int) {
        modo.forward(Screens.MediaList(type))
    }

    companion object {
        fun create() = HomeFragment()
    }
}
