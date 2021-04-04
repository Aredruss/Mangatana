package com.aredruss.mangatana.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.App
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.ActivityMainBinding
import com.aredruss.mangatana.modo.Screens
import com.github.terrakok.modo.android.AppScreen
import com.github.terrakok.modo.android.ModoRender
import com.github.terrakok.modo.android.init
import com.github.terrakok.modo.back
import com.github.terrakok.modo.forward
import timber.log.Timber

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding(R.id.main_cl)
    private val modo = App.INSTANCE.modo

    var menu: Menu? = null

    private val modoRender by lazy {
        object : ModoRender(this@MainActivity, R.id.main_host_fr) {

            override fun setupTransaction(
                fragmentManager: FragmentManager,
                transaction: FragmentTransaction,
                screen: AppScreen,
                newFragment: Fragment
            ) {
                transaction.setCustomAnimations(
                    R.anim.fragment_fade_enter,
                    R.anim.fragment_fade_exit,
                    R.anim.fragment_fade_enter,
                    R.anim.fragment_fade_exit
                )

                menu?.forEach {
                    it.isVisible = screen !is Screens.About && screen !is Screens.Settings
                }
                super.setupTransaction(fragmentManager, transaction, screen, newFragment)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            setContentView(root)
            setSupportActionBar(binding.mainTb)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        modo.init(savedInstanceState, Screens.Home())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                modo.forward(Screens.About())
            }
            R.id.action_settings -> {
                modo.forward(Screens.Settings())
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        modo.render = modoRender
    }

    override fun onPause() {
        modo.render = null
        super.onPause()
    }

    override fun onBackPressed() {
        modo.back()
    }

    override fun onSupportNavigateUp(): Boolean {
        modo.back()
        Timber.e("AAAAAAAAAAAAAAAAA")
        return true
    }
}
