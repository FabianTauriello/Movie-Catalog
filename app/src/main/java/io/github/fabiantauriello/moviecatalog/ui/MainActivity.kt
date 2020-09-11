package io.github.fabiantauriello.moviecatalog.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.databinding.ActivityMainBinding


/**
 * Main entry point into the application
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )

        val navController = findNavController(R.id.navHostFrag)

        binding.bottomNav.setupWithNavController(navController)
    }

}