package com.playgroundagc.deepltranslator.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    companion object {
        private lateinit var binding: ActivityAppBinding
        private lateinit var appBarConfiguration: AppBarConfiguration
    }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_app)

        setupNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentApp)
        return navController.navigateUp(appBarConfiguration)
                //|| super.onSupportNavigateUp()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentApp) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}