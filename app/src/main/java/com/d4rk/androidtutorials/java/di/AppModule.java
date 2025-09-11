package com.d4rk.androidtutorials.java.di;

import android.app.Application;
import android.content.res.AssetManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.d4rk.androidtutorials.java.data.repository.DefaultHomeRepository;
import com.d4rk.androidtutorials.java.data.repository.DefaultMainRepository;
import com.d4rk.androidtutorials.java.data.repository.DefaultQuizRepository;
import com.d4rk.androidtutorials.java.data.repository.DefaultSupportRepository;
import com.d4rk.androidtutorials.java.data.repository.HomeRepository;
import com.d4rk.androidtutorials.java.data.repository.MainRepository;
import com.d4rk.androidtutorials.java.data.repository.QuizRepository;
import com.d4rk.androidtutorials.java.data.repository.SupportRepository;
import com.d4rk.androidtutorials.java.data.source.DefaultHomeLocalDataSource;
import com.d4rk.androidtutorials.java.data.source.DefaultHomeRemoteDataSource;
import com.d4rk.androidtutorials.java.data.source.DefaultQuizLocalDataSource;
import com.d4rk.androidtutorials.java.data.source.HomeLocalDataSource;
import com.d4rk.androidtutorials.java.data.source.HomeRemoteDataSource;
import com.d4rk.androidtutorials.java.data.source.QuizLocalDataSource;
import com.d4rk.androidtutorials.java.domain.about.GetCurrentYearUseCase;
import com.d4rk.androidtutorials.java.domain.about.GetVersionStringUseCase;
import com.d4rk.androidtutorials.java.domain.help.LaunchReviewFlowUseCase;
import com.d4rk.androidtutorials.java.domain.help.RequestReviewFlowUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetDailyTipUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPromotedAppsUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPlayStoreUrlUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetAppPlayStoreUrlUseCase;
import com.d4rk.androidtutorials.java.domain.main.ApplyLanguageSettingsUseCase;
import com.d4rk.androidtutorials.java.domain.main.ApplyThemeSettingsUseCase;
import com.d4rk.androidtutorials.java.domain.main.BuildShortcutIntentUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetAppUpdateManagerUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetBottomNavLabelVisibilityUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetDefaultTabPreferenceUseCase;
import com.d4rk.androidtutorials.java.domain.main.IsAppInstalledUseCase;
import com.d4rk.androidtutorials.java.domain.main.MarkStartupScreenShownUseCase;
import com.d4rk.androidtutorials.java.domain.main.ShouldShowStartupScreenUseCase;
import com.d4rk.androidtutorials.java.domain.quiz.LoadQuizQuestionsUseCase;
import com.d4rk.androidtutorials.java.domain.settings.ApplyConsentUseCase;
import com.d4rk.androidtutorials.java.domain.settings.GetDarkModeUseCase;
import com.d4rk.androidtutorials.java.domain.settings.RegisterPreferenceChangeListenerUseCase;
import com.d4rk.androidtutorials.java.domain.settings.UnregisterPreferenceChangeListenerUseCase;
import com.d4rk.androidtutorials.java.domain.settings.SetConsentAcceptedUseCase;
import com.d4rk.androidtutorials.java.domain.settings.OnPreferenceChangedUseCase;
import com.d4rk.androidtutorials.java.domain.startup.LoadConsentFormUseCase;
import com.d4rk.androidtutorials.java.domain.startup.RequestConsentInfoUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitBillingClientUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitMobileAdsUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitiatePurchaseUseCase;
import com.d4rk.androidtutorials.java.domain.support.QueryProductDetailsUseCase;
import com.d4rk.androidtutorials.java.ui.screens.about.repository.AboutRepository;
import com.d4rk.androidtutorials.java.ui.screens.help.repository.HelpRepository;
import com.d4rk.androidtutorials.java.ui.screens.settings.repository.SettingsRepository;
import com.d4rk.androidtutorials.java.ui.screens.startup.repository.StartupRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public ExecutorService provideExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    public RequestQueue provideRequestQueue(Application application) {
        return Volley.newRequestQueue(application);
    }

    @Provides
    @Singleton
    public HomeRemoteDataSource provideHomeRemoteDataSource(RequestQueue queue) {
        return new DefaultHomeRemoteDataSource(queue,
                "https://raw.githubusercontent.com/D4rK7355608/com.d4rk.apis/refs/heads/main/App%20Toolkit/release/en/home/api_android_apps.json");
    }

    @Provides
    @Singleton
    public HomeLocalDataSource provideHomeLocalDataSource(Application application) {
        return new DefaultHomeLocalDataSource(application);
    }

    @Provides
    @Singleton
    public HomeRepository provideHomeRepository(HomeRemoteDataSource remote,
                                                HomeLocalDataSource local) {
        return new DefaultHomeRepository(remote, local);
    }

    @Provides
    public GetDailyTipUseCase provideGetDailyTipUseCase(HomeRepository repository) {
        return new GetDailyTipUseCase(repository);
    }

    @Provides
    public GetPromotedAppsUseCase provideGetPromotedAppsUseCase(HomeRepository repository) {
        return new GetPromotedAppsUseCase(repository);
    }

    @Provides
    public GetPlayStoreUrlUseCase provideGetPlayStoreUrlUseCase(HomeRepository repository) {
        return new GetPlayStoreUrlUseCase(repository);
    }

    @Provides
    public GetAppPlayStoreUrlUseCase provideGetAppPlayStoreUrlUseCase(HomeRepository repository) {
        return new GetAppPlayStoreUrlUseCase(repository);
    }

    @Provides
    @Singleton
    public AboutRepository provideAboutRepository(Application application) {
        return new AboutRepository(application);
    }

    @Provides
    public GetVersionStringUseCase provideGetVersionStringUseCase(AboutRepository repository) {
        return new GetVersionStringUseCase(repository);
    }

    @Provides
    public GetCurrentYearUseCase provideGetCurrentYearUseCase(AboutRepository repository) {
        return new GetCurrentYearUseCase(repository);
    }

    @Provides
    @Singleton
    public MainRepository provideMainRepository(Application application) {
        return new DefaultMainRepository(application);
    }

    @Provides
    public ApplyThemeSettingsUseCase provideApplyThemeSettingsUseCase(MainRepository repository) {
        return new ApplyThemeSettingsUseCase(repository);
    }

    @Provides
    public GetBottomNavLabelVisibilityUseCase provideGetBottomNavLabelVisibilityUseCase(MainRepository repository) {
        return new GetBottomNavLabelVisibilityUseCase(repository);
    }

    @Provides
    public GetDefaultTabPreferenceUseCase provideGetDefaultTabPreferenceUseCase(MainRepository repository) {
        return new GetDefaultTabPreferenceUseCase(repository);
    }

    @Provides
    public ApplyLanguageSettingsUseCase provideApplyLanguageSettingsUseCase(MainRepository repository) {
        return new ApplyLanguageSettingsUseCase(repository);
    }

    @Provides
    public ShouldShowStartupScreenUseCase provideShouldShowStartupScreenUseCase(MainRepository repository) {
        return new ShouldShowStartupScreenUseCase(repository);
    }

    @Provides
    public MarkStartupScreenShownUseCase provideMarkStartupScreenShownUseCase(MainRepository repository) {
        return new MarkStartupScreenShownUseCase(repository);
    }

    @Provides
    public IsAppInstalledUseCase provideIsAppInstalledUseCase(MainRepository repository) {
        return new IsAppInstalledUseCase(repository);
    }

    @Provides
    public BuildShortcutIntentUseCase provideBuildShortcutIntentUseCase(MainRepository repository) {
        return new BuildShortcutIntentUseCase(repository);
    }

    @Provides
    public GetAppUpdateManagerUseCase provideGetAppUpdateManagerUseCase(MainRepository repository) {
        return new GetAppUpdateManagerUseCase(repository);
    }

    @Provides
    @Singleton
    public SettingsRepository provideSettingsRepository(Application application) {
        return new SettingsRepository(application);
    }

    @Provides
    public OnPreferenceChangedUseCase provideOnPreferenceChangedUseCase(SettingsRepository repository) {
        return new OnPreferenceChangedUseCase(repository);
    }

    @Provides
    public ApplyConsentUseCase provideApplyConsentUseCase(SettingsRepository repository) {
        return new ApplyConsentUseCase(repository);
    }

    @Provides
    public RegisterPreferenceChangeListenerUseCase provideRegisterPreferenceChangeListenerUseCase(SettingsRepository repository) {
        return new RegisterPreferenceChangeListenerUseCase(repository);
    }

    @Provides
    public UnregisterPreferenceChangeListenerUseCase provideUnregisterPreferenceChangeListenerUseCase(SettingsRepository repository) {
        return new UnregisterPreferenceChangeListenerUseCase(repository);
    }

    @Provides
    public GetDarkModeUseCase provideGetDarkModeUseCase(SettingsRepository repository) {
        return new GetDarkModeUseCase(repository);
    }

    @Provides
    public SetConsentAcceptedUseCase provideSetConsentAcceptedUseCase(SettingsRepository repository) {
        return new SetConsentAcceptedUseCase(repository);
    }

    @Provides
    @Singleton
    public QuizLocalDataSource provideQuizLocalDataSource(Application application, ExecutorService executorService) {
        AssetManager manager = application.getAssets();
        return new DefaultQuizLocalDataSource(manager, executorService);
    }

    @Provides
    @Singleton
    public QuizRepository provideQuizRepository(QuizLocalDataSource local) {
        return new DefaultQuizRepository(local);
    }

    @Provides
    public LoadQuizQuestionsUseCase provideLoadQuizQuestionsUseCase(QuizRepository repository) {
        return new LoadQuizQuestionsUseCase(repository);
    }

    @Provides
    @Singleton
    public StartupRepository provideStartupRepository(Application application) {
        return new StartupRepository(application);
    }

    @Provides
    public RequestConsentInfoUseCase provideRequestConsentInfoUseCase(StartupRepository repository) {
        return new RequestConsentInfoUseCase(repository);
    }

    @Provides
    public LoadConsentFormUseCase provideLoadConsentFormUseCase(StartupRepository repository) {
        return new LoadConsentFormUseCase(repository);
    }

    @Provides
    @Singleton
    public SupportRepository provideSupportRepository(Application application) {
        return new DefaultSupportRepository(application);
    }

    @Provides
    public InitBillingClientUseCase provideInitBillingClientUseCase(SupportRepository repository) {
        return new InitBillingClientUseCase(repository);
    }

    @Provides
    public QueryProductDetailsUseCase provideQueryProductDetailsUseCase(SupportRepository repository) {
        return new QueryProductDetailsUseCase(repository);
    }

    @Provides
    public InitiatePurchaseUseCase provideInitiatePurchaseUseCase(SupportRepository repository) {
        return new InitiatePurchaseUseCase(repository);
    }

    @Provides
    public InitMobileAdsUseCase provideInitMobileAdsUseCase(SupportRepository repository) {
        return new InitMobileAdsUseCase(repository);
    }

    @Provides
    @Singleton
    public HelpRepository provideHelpRepository(Application application) {
        return new HelpRepository(application);
    }

    @Provides
    public RequestReviewFlowUseCase provideRequestReviewFlowUseCase(HelpRepository repository) {
        return new RequestReviewFlowUseCase(repository);
    }

    @Provides
    public LaunchReviewFlowUseCase provideLaunchReviewFlowUseCase(HelpRepository repository) {
        return new LaunchReviewFlowUseCase(repository);
    }
}
