package com.isteam.movieappaz.presentation.main

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.network.ConnectivityObserver
import com.isteam.movieappaz.common.utils.ThemeEnum
import com.isteam.movieappaz.common.utils.changeLanguage
import com.isteam.movieappaz.common.utils.checkForAppUpdates
import com.isteam.movieappaz.common.utils.createProgressDialog
import com.isteam.movieappaz.common.utils.gone
import com.isteam.movieappaz.common.utils.showMotionToast
import com.isteam.movieappaz.common.utils.showNetworkDialog
import com.isteam.movieappaz.common.utils.visible
import com.isteam.movieappaz.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToastStyle


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            askNotificationPermission()
        }
    }

    private val viewModel by viewModels<MainViewModel>()
    private var job: Job? = null
    private var isConnectionHas = false

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    }

    private val navController by lazy { navHostFragment.navController }

    private val updateActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) {
            showMotionToast(
                resources.getString(R.string.error),
                resources.getString(R.string.update_failed),
                MotionToastStyle.ERROR
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getUpdateStatus()
        observeData()
        setup()
        askNotificationPermission()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showMotionToast(
                    resources.getString(R.string.info),
                    resources.getString(R.string.no_permission_for_notifications)
                )
            }
        }
    }

    private fun observeThemeChanges() {
        when (viewModel.getThemeFromSP()) {
            ThemeEnum.DARK_MODE -> setTheme(ThemeEnum.DARK_MODE)
            ThemeEnum.LIGHT_MODE -> setTheme(ThemeEnum.LIGHT_MODE)
        }
    }

    private fun setTheme(themeEnum: ThemeEnum) {
        viewModel.setTheme(themeEnum)
        changeLanguage(viewModel.getLanguageCode())
        when (themeEnum) {
            ThemeEnum.DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ThemeEnum.LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        if (viewModel.isInitializedTheme()) {
            recreateNavigation()
        }
    }

    private fun recreateNavigation() {
        val currentId = navController.currentDestination?.id
        currentId?.let {

            if (it == R.id.movieDetailsFragment) {
                navController.navigate(R.id.homeFragment)
            } else if (it != R.id.profileFragment && it != R.id.settingsFragment) {
                navController.popBackStack()
                navController.navigate(it)
            } else {
                Unit
            }
        }
    }

    private fun getUpdateStatus() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkForAppUpdates(
            appUpdateManager,
            updateType,
            updateActivityResultLauncher
        )

    }

    private fun setup() {
        setBottom()
    }

    private fun observeData() {
        val pd = this.createProgressDialog()
        val dialog = this.showNetworkDialog(
            getString(R.string.network_message),
            positiveAction = {
                this.finish()
            },
            negativeAction = {
                job?.cancel()
                pd.show()
                job = lifecycleScope.launch {
                    delay(5000L)
                    pd.cancel()
                    if (!isConnectionHas) {
                        it.show()
                    }
                }
                it.show()
            })
        viewModel.state.observe(this) {
            when (it) {
                is MainUiState.NetworkStatus -> {
                    when (it.status) {
                        ConnectivityObserver.ConnectionStatus.Available -> {
                            isConnectionHas = true
                            job?.cancel()
                            if (dialog.isShowing) dialog.cancel()
                            pd.cancel()
                        }

                        else -> {
                            otherNetworkStatus(pd, dialog)
                        }
                    }
                }

                is MainUiState.Language -> {
                    changeLanguage(it.lanCode)
                    binding.bottomMain.menu.clear()
                    binding.bottomMain.inflateMenu(R.menu.bottom_menu)
                }

                else -> Unit
            }
        }
    }

    private fun setBottom() {
        with(binding) {
            bottomMain.itemIconTintList = null

            bottomMain.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.shareStoryFragment,
                    R.id.settingsFragment,
                    R.id.onboardingLanguageFragment,
                    R.id.onboardingFragment,
                    R.id.notificationsFragment,
                    R.id.movieReviewsFragment,
                    R.id.celebrityDetailsFragment,
                    R.id.movieImagesFragment,
                    R.id.aiShowCaseFragment,
                    R.id.splashFragment,
                    R.id.loginFragment,
                    R.id.registerFragment,
                    R.id.createUserFragment,
                    R.id.forgotPassFragment,
                    R.id.watchListDetailFragment,
                    R.id.movieListFragment,
                    R.id.editProfileFragment,
                    R.id.movieDetailsFragment,
                    R.id.imagePreviewFragment -> if (bottomMain.isVisible) bottomMain.gone()

                    else -> if (!bottomMain.isVisible) bottomMain.visible()
                }
            }


            bottomMain.setOnNavigationItemReselectedListener {
                // should be empty ,
                // because you will ignore do anything
                // when you re-click to selected item.
                //                           Bashirli.
            }

        }
    }

    override fun onStart() {
        super.onStart()
        observeThemeChanges()
    }


    private fun otherNetworkStatus(pd: Dialog, dialog: Dialog) {
        pd.cancel()
        dialog.show()
        isConnectionHas = false
    }
}