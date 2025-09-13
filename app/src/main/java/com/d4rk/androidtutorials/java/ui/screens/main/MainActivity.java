package com.d4rk.androidtutorials.java.ui.screens.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityMainBinding;
import com.d4rk.androidtutorials.java.notifications.managers.AppUpdateNotificationsManager;
import com.d4rk.androidtutorials.java.notifications.managers.AppUsageNotificationsManager;
import com.d4rk.androidtutorials.java.ui.components.navigation.BottomSheetMenuFragment;
import com.d4rk.androidtutorials.java.ui.screens.startup.StartupActivity;
import com.d4rk.androidtutorials.java.ui.screens.startup.StartupViewModel;
import com.d4rk.androidtutorials.java.ui.screens.support.SupportActivity;
import com.d4rk.androidtutorials.java.utils.ConsentUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.d4rk.androidtutorials.java.utils.ReviewHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationBarView;
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

    private static final long BACK_PRESS_INTERVAL = 2000;
    private final ActivityResultLauncher<IntentSenderRequest> updateActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartIntentSenderForResult(),
                    result -> {
                        if (result.getResultCode() != Activity.RESULT_OK) {
                            Log.d("MainActivity", "In-app update flow failed! " + result.getResultCode());
                        }
                    }
            );
    private final SparseIntArray navOrder = new SparseIntArray();
    private ActivityMainBinding mBinding;
    private final DefaultLifecycleObserver lifecycleObserver = new DefaultLifecycleObserver() {
        @Override
        public void onResume(@NonNull LifecycleOwner owner) {
            ConsentUtils.applyStoredConsent(MainActivity.this);
            if (mBinding != null && mBinding.adView != null) {
                if (ConsentUtils.canShowAds(MainActivity.this)) {
                    if (mBinding.adView.getVisibility() != View.VISIBLE) {
                        MobileAds.initialize(MainActivity.this);
                        mBinding.adPlaceholder.setVisibility(View.GONE);
                        mBinding.adView.setVisibility(View.VISIBLE);
                        mBinding.adView.loadAd(new AdRequest.Builder().build());
                    }
                } else {
                    mBinding.adView.setVisibility(View.GONE);
                    mBinding.adPlaceholder.setVisibility(View.VISIBLE);
                }
            }
        }
    };
    private MainViewModel mainViewModel;
    private NavController navController;
    private int currentNavIndex;
    private AppUpdateManager appUpdateManager;
    private AppUpdateNotificationsManager appUpdateNotificationsManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean(getString(R.string.key_onboarding_complete), false)) {
            startActivity(new Intent(this, StartupActivity.class));
            finish();
            return;
        }
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Fallback: show the consent form again if required.
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(this);
        if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
            StartupViewModel consentViewModel = new ViewModelProvider(this).get(StartupViewModel.class);
            ConsentRequestParameters params = new ConsentRequestParameters.Builder().build();
            consentViewModel.requestConsentInfoUpdate(this, params,
                    () -> consentViewModel.loadConsentForm(this, null), null);
        }

        setupActionBar();
        observeViewModel();


        String[] themeValues = getResources().getStringArray(R.array.preference_theme_values);
        String[] bottomNavBarLabelsValues = getResources().getStringArray(R.array.preference_bottom_navigation_bar_labels_values);
        String[] defaultTabValues = getResources().getStringArray(R.array.preference_default_tab_values);
        mainViewModel.applySettings(themeValues, bottomNavBarLabelsValues, defaultTabValues);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        this.appUpdateManager = mainViewModel.getAppUpdateManager();
        setupUpdateNotifications();

        registerInstallStateListener();
        getLifecycle().addObserver(lifecycleObserver);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                long currentTime = System.currentTimeMillis();
                if (currentTime - backPressedTime < BACK_PRESS_INTERVAL) {
                    finish();
                    moveTaskToBack(true);
                } else {
                    backPressedTime = currentTime;
                    Toast.makeText(MainActivity.this, R.string.press_back_again_to_exit, Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkInAppReview();
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


    private boolean shouldUseNavigationRail() {
        return getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    private void observeViewModel() {
        mainViewModel.getUiState().observe(this, uiState -> {
            if (uiState == null) {
                return;
            }

            boolean useRail = shouldUseNavigationRail();
            EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
            ActivityMainBinding binding = mBinding;
            if (binding == null) {
                return;
            }
            NavigationBarView navBarView = (NavigationBarView) binding.navView;
            if (useRail) {
                binding.navRail.setVisibility(View.VISIBLE);
                navBarView.setVisibility(View.GONE);
                edgeToEdgeDelegate.applyEdgeToEdge(binding.container);
            } else {
                binding.navRail.setVisibility(View.GONE);
                navBarView.setVisibility(View.VISIBLE);
                edgeToEdgeDelegate.applyEdgeToEdgeBottomBar(binding.container, navBarView);

                navBarView.setLabelVisibilityMode(uiState.bottomNavVisibility());
                if (binding.adView != null) {
                    if (ConsentUtils.canShowAds(this)) {
                        MobileAds.initialize(this);
                        binding.adPlaceholder.setVisibility(View.GONE);
                        binding.adView.setVisibility(View.VISIBLE);
                        binding.adView.loadAd(new AdRequest.Builder().build());
                    } else {
                        binding.adView.setVisibility(View.GONE);
                        binding.adPlaceholder.setVisibility(View.VISIBLE);
                    }
                }
            }

            NavHostFragment navHostFragment = (NavHostFragment)
                    getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
            if (navHostFragment != null) {
                navController = navHostFragment.getNavController();
                NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);
                navGraph.setStartDestination(uiState.defaultNavDestination());
                navController.setGraph(navGraph);

                navOrder.put(R.id.navigation_home, 0);
                navOrder.put(R.id.navigation_android_studio, 1);
                navOrder.put(R.id.navigation_about, 2);
                androidx.navigation.NavDestination destination = navController.getCurrentDestination();
                if (destination != null) {
                    currentNavIndex = navOrder.get(destination.getId());
                }

                NavOptions forwardOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.fragment_spring_enter)
                        .setExitAnim(R.anim.fragment_spring_exit)
                        .setPopEnterAnim(R.anim.fragment_spring_pop_enter)
                        .setPopExitAnim(R.anim.fragment_spring_pop_exit)
                        .build();

                NavOptions backwardOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.fragment_spring_pop_enter)
                        .setExitAnim(R.anim.fragment_spring_pop_exit)
                        .setPopEnterAnim(R.anim.fragment_spring_enter)
                        .setPopExitAnim(R.anim.fragment_spring_exit)
                        .build();

                if (useRail) {
                    if (mBinding.navRail != null) {
                        NavigationUI.setupWithNavController(mBinding.navRail, navController);
                        mBinding.navRail.setOnItemSelectedListener(item -> {
                            if (item.getItemId() == navController.getCurrentDestination().getId()) {
                                return true;
                            }
                            int newIndex = navOrder.get(item.getItemId());
                            NavOptions options = newIndex > currentNavIndex ? forwardOptions : backwardOptions;
                            navController.navigate(item.getItemId(), null, options);
                            currentNavIndex = newIndex;
                            return true;
                        });
                    }
                } else {
                    NavigationUI.setupWithNavController(navBarView, navController);
                    navBarView.setOnItemSelectedListener(item -> {
                        if (item.getItemId() == navController.getCurrentDestination().getId()) {
                            return true;
                        }
                        int newIndex = navOrder.get(item.getItemId());
                        NavOptions options = newIndex > currentNavIndex ? forwardOptions : backwardOptions;
                        navController.navigate(item.getItemId(), null, options);
                        currentNavIndex = newIndex;
                        return true;
                    });
                }

                setSupportActionBar(mBinding.toolbar);

                mBinding.toolbar.setNavigationOnClickListener(v -> {
                    BottomSheetMenuFragment bottomSheet = new BottomSheetMenuFragment();
                    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
                });
            }

            if (uiState.themeChanged()) {
                recreate();
            }
        });

        mainViewModel.getLoadingState().observe(this, isLoading -> {
            if (mBinding != null) {
                mBinding.progressBar.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE);
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        AppUsageNotificationsManager appUsageNotificationsManager = new AppUsageNotificationsManager(this);
        appUsageNotificationsManager.scheduleAppUsageCheck();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appUpdateNotificationsManager.checkAndSendUpdateNotification();
        }
        checkForFlexibleOrImmediateUpdate();
    }

    private void checkForFlexibleOrImmediateUpdate() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                    boolean updateAvailable = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE;
                    if (updateAvailable) {
                        startImmediateUpdate(appUpdateInfo);
                    }
                })
                .addOnFailureListener(e -> {
                    if (!BuildConfig.DEBUG) {
                        Snackbar.make(
                                findViewById(android.R.id.content),
                                getString(R.string.snack_general_error),
                                Snackbar.LENGTH_LONG
                        ).show();
                    }
        });
    }

    private void checkInAppReview() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int sessionCount = prefs.getInt(getString(R.string.key_session_count), 0);
        boolean hasPrompted = prefs.getBoolean(getString(R.string.key_has_prompted_review), false);

        ReviewHelper.launchInAppReviewIfEligible(
                this,
                sessionCount,
                hasPrompted,
                () -> prefs.edit().putBoolean(getString(R.string.key_has_prompted_review), true).apply()
        );

        prefs.edit().putInt(getString(R.string.key_session_count), sessionCount + 1).apply();
    }

    private void startImmediateUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateActivityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            );
        } catch (Exception e) {
            Log.e("MainActivity", "Error starting in-app update", e);
        }
    }

    private void setupUpdateNotifications() {
        appUpdateNotificationsManager = new AppUpdateNotificationsManager(this);
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