package com.aredruss.mangatana.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aredruss.mangatana.App
import com.aredruss.mangatana.R
import com.aredruss.mangatana.databinding.ActivityMainBinding
import com.aredruss.mangatana.modo.Screens
import com.github.terrakok.modo.ModoRender
import com.github.terrakok.modo.init

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(R.id.main_cl)

    private val modo = App.INSTANCE.modo
    private val modoRender by lazy { ModoRender(this@MainActivity, R.id.main_host_fr) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            setContentView(root)
        }

        modo.init(savedInstanceState, modoRender, Screens.Home())
    }

    override fun onResume() {
        super.onResume()
        modo.render = modoRender
    }

    override fun onPause() {
        modo.render = null
        super.onPause()
    }
}