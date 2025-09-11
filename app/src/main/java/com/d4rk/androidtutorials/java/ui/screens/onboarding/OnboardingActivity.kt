package com.d4rk.androidtutorials.java.ui.screens.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.d4rk.androidtutorials.java.ui.screens.main.MainActivity
import androidx.lifecycle.viewmodel.compose.viewModel

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: OnboardingViewModel = viewModel()
            OnboardingScreen(viewModel) {
                viewModel.completeOnboarding()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
