package com.playgroundagc.deepltranslator.presentation.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.playgroundagc.core.repository.RepositoryImpl
import com.playgroundagc.core.repository.translation.TranslationApi
import com.playgroundagc.core.repository.translation.TranslationRemoteDataSourceImpl
import com.playgroundagc.core.usecase.GetAPIUsageUC
import com.playgroundagc.core.usecase.TranslateTextUC
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.databinding.ActivityAppBinding
import com.playgroundagc.deepltranslator.presentation.fragment.FragmentViewModel
import com.playgroundagc.deepltranslator.presentation.fragment.FragmentViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
class AppActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    companion object {
        private lateinit var binding: ActivityAppBinding
        private lateinit var appBarConfiguration: AppBarConfiguration
        lateinit var viewModel: FragmentViewModel
    }

    //private val viewModel: FragmentViewModel by viewModels()

    lateinit var navController: NavController

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        val translationApi = TranslationApi
        val translationRemoteDSI = TranslationRemoteDataSourceImpl(translationApi)
        //val textLocalDSI = TextService()
        val repository = RepositoryImpl(translationRemoteDSI)
        val translateTextUC = TranslateTextUC(repository)
        val getAPIUsageUC = GetAPIUsageUC(repository)
        //viewModel = ViewModelProvider(this, FragmentViewModelFactory(translateTextUC, getAPIUsageUC))[FragmentViewModel::class.java]

        binding = DataBindingUtil.setContentView(this, R.layout.activity_app)

        setupNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentApp)
        return navController.navigateUp(appBarConfiguration)
        //|| super.onSupportNavigateUp()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home -> {

                //finish()

                return true
            }
        }
        return super.onContextItemSelected(item)
    }
    //endregion

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentApp) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.languageSelectFragment -> {
                //arrowVisibility(false)
            }
            else -> {
                //arrowVisibility(true)
            }
        }
    }

    private fun arrowVisibility(value: Boolean = true) {
        val actionBar = actionBar

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
            actionBar.setDisplayHomeAsUpEnabled(value);
        }
    }
}