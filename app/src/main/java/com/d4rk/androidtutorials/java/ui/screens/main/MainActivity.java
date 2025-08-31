package com.d4rk.androidtutorials.java.ui.screens.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityMainBinding;
import com.d4rk.androidtutorials.java.notifications.managers.AppUpdateNotificationsManager;
import com.d4rk.androidtutorials.java.ui.components.navigation.BottomSheetMenuFragment;
import com.d4rk.androidtutorials.java.ui.screens.startup.StartupViewModel;
import com.d4rk.androidtutorials.java.ui.screens.startup.dialogs.ConsentDialogFragment;
import com.d4rk.androidtutorials.java.ui.screens.support.SupportActivity;
import com.d4rk.androidtutorials.java.utils.ConsentUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigationrail.NavigationRailView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<IntentSenderRequest> updateActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartIntentSenderForResult(),
                    result -> {
                        if (result.getResultCode() != Activity.RESULT_OK) {
                            Log.d("MainActivity", "In-app update flow failed! " + result.getResultCode());
                        }
                    }
            );
    private ActivityMainBinding mBinding;
    private MainViewModel mainViewModel;
    private StartupViewModel startupViewModel;
    private ConsentInformation consentInformation;
    private NavController navController;
    private AppUpdateNotificationsManager appUpdateNotificationsManager;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private final DefaultLifecycleObserver lifecycleObserver = new DefaultLifecycleObserver() {
        @Override
        public void onResume(@NonNull LifecycleOwner owner) {
            ConsentUtils.applyStoredConsent(MainActivity.this);
            if (mBinding.adView != null) {
                if (ConsentUtils.canShowAds(MainActivity.this)) {
                    if (mBinding.adView.getVisibility() != View.VISIBLE) {
                        MobileAds.initialize(MainActivity.this);
                        mBinding.adView.setVisibility(View.VISIBLE);
                        mBinding.adView.loadAd(new AdRequest.Builder().build());
                    }
                } else {
                    mBinding.adView.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        startupViewModel = new ViewModelProvider(this).get(StartupViewModel.class);
        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        ConsentRequestParameters params = new ConsentRequestParameters.Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();
        startupViewModel.requestConsentInfoUpdate(
                this,
                params,
                () -> {
                    if (consentInformation.isConsentFormAvailable()) {
                        startupViewModel.loadConsentForm(this, null);
                    }
                },
                null
        );

        setupActionBar();
        observeViewModel();

        setupUpdateNotifications();

        String[] themeValues = getResources().getStringArray(R.array.preference_theme_values);
        String[] bottomNavBarLabelsValues = getResources().getStringArray(R.array.preference_bottom_navigation_bar_labels_values);
        String[] defaultTabValues = getResources().getStringArray(R.array.preference_default_tab_values);
        mainViewModel.applySettings(themeValues, bottomNavBarLabelsValues, defaultTabValues);
        if (mainViewModel.shouldShowStartupScreen()) {
            mainViewModel.markStartupScreenShown();
            showConsentDialog();
        }

        launcherShortcuts();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        this.appUpdateManager = mainViewModel.getAppUpdateManager();

        registerInstallStateListener();
        getLifecycle().addObserver(lifecycleObserver);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle(R.string.alert_dialog_close)
                        .setMessage(R.string.summary_alert_dialog_close)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            finish();
                            moveTaskToBack(true);
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            new AppBarConfiguration.Builder(
                    R.id.navigation_home,
                    R.id.navigation_android_studio,
                    R.id.navigation_about
            );
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    private void launcherShortcuts() {
        boolean isInstalled = mainViewModel.isAndroidTutorialsInstalled();

        ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(this, "shortcut_id")
                .setShortLabel(getString(R.string.shortcut_kotlin_edition_short))
                .setLongLabel(getString(R.string.shortcut_kotlin_edition_long))
                .setIcon(IconCompat.createWithResource(this, R.mipmap.ic_shortcut_kotlin_edition))
                .setIntent(mainViewModel.getShortcutIntent(isInstalled))
                .build();

        ShortcutManagerCompat.pushDynamicShortcut(this, shortcut);
    }

    private void observeViewModel() {
        mainViewModel.getUiState().observe(this, uiState -> {
            if (uiState == null) {
                return;
            }

            EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
            if (mBinding.navView instanceof BottomNavigationView) {

                edgeToEdgeDelegate.applyEdgeToEdgeBottomBar(mBinding.container, mBinding.navView);

                ((BottomNavigationView) mBinding.navView).setLabelVisibilityMode(uiState.getBottomNavVisibility());
                if (mBinding.adView != null) {
                    if (ConsentUtils.canShowAds(this)) {
                        MobileAds.initialize(this);
                        mBinding.adView.setVisibility(View.VISIBLE);
                        mBinding.adView.loadAd(new AdRequest.Builder().build());
                    } else {
                        mBinding.adView.setVisibility(View.GONE);
                    }
                }
            } else {
                edgeToEdgeDelegate.applyEdgeToEdge(mBinding.container);
            }

            NavHostFragment navHostFragment = (NavHostFragment)
                    getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
            if (navHostFragment != null) {
                navController = navHostFragment.getNavController();
                NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);
                navGraph.setStartDestination(uiState.getDefaultNavDestination());
                navController.setGraph(navGraph);

                NavOptions springNavOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.fragment_spring_enter)
                        .setExitAnim(R.anim.fragment_spring_exit)
                        .setPopEnterAnim(R.anim.fragment_spring_pop_enter)
                        .setPopExitAnim(R.anim.fragment_spring_pop_exit)
                        .build();

                if (mBinding.navView instanceof BottomNavigationView bottomNav) {
                    NavigationUI.setupWithNavController(bottomNav, navController);
                    bottomNav.setOnItemSelectedListener(item -> {
                        navController.navigate(item.getItemId(), null, springNavOptions);
                        return true;
                    });
                } else if (mBinding.navView instanceof NavigationRailView railView) {
                    NavigationUI.setupWithNavController(railView, navController);
                    railView.setOnItemSelectedListener(item -> {
                        navController.navigate(item.getItemId(), null, springNavOptions);
                        return true;
                    });
                }

                setSupportActionBar(mBinding.toolbar);

                mBinding.toolbar.setNavigationOnClickListener(v -> {
                    BottomSheetMenuFragment bottomSheet = new BottomSheetMenuFragment();
                    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
                });
            }

            if (uiState.isThemeChanged()) {
                recreate();
            }
        });
    }

    private void setupUpdateNotifications() {
        appUpdateNotificationsManager = new AppUpdateNotificationsManager(this);
    }

    private void showConsentDialog() {
        ConsentDialogFragment dialog = new ConsentDialogFragment();
        dialog.setConsentListener((analytics, adStorage, adUserData, adPersonalization) ->
                ConsentUtils.updateFirebaseConsent(this, analytics, adStorage, adUserData, adPersonalization));
        dialog.show(getSupportFragmentManager(), "consent_dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.support) {
            startActivity(new Intent(this, SupportActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkForImmediateUpdate() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            boolean updateAvailable =
                                    appUpdateInfo.updateAvailability()
                                            == UpdateAvailability.UPDATE_AVAILABLE;
                            if (updateAvailable
                                    && appUpdateInfo.isUpdateTypeAllowed(
                                            AppUpdateType.IMMEDIATE)) {
                                startImmediateUpdate(appUpdateInfo);
                            }
                        })
                .addOnFailureListener(
                        e -> {
                            if (!BuildConfig.DEBUG) {
                                Snackbar.make(
                                                findViewById(android.R.id.content),
                                                getString(R.string.snack_general_error),
                                                Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        });
    }

    private void startImmediateUpdate(AppUpdateInfo appUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                updateActivityResultLauncher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
        );
    }

    private void registerInstallStateListener() {
        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        };
        appUpdateManager.registerListener(installStateUpdatedListener);
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar.make(
                        mBinding.getRoot(),
                        getString(R.string.update_downloaded),
                        Snackbar.LENGTH_INDEFINITE
                )
                .setAction(getString(R.string.alert_dialog_require_restart), v -> appUpdateManager.completeUpdate())
                .show();
    }

    @Override
    protected void onDestroy() {
        if (installStateUpdatedListener != null && appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
        super.onDestroy();
    }
}