package com.d4rk.androidtutorials.java.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.androidtutorials.java.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = {3})
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            when (page) {
                0 -> ThemePage(viewModel)
                1 -> DefaultTabPage(viewModel)
                else -> BottomBarPage(viewModel, onFinish)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (pagerState.currentPage > 0) {
                TextButton(onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } }) {
                    Text(stringResource(R.string.back))
                }
            }
            if (pagerState.currentPage < 2) {
                TextButton(onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }) {
                    Text(stringResource(R.string.next))
                }
            }
        }
    }
}

@Composable
private fun ThemePage(viewModel: OnboardingViewModel) {
    val entries = stringArrayResource(R.array.preference_theme_entries)
    val values = stringArrayResource(R.array.preference_theme_values)
    var selected by remember { mutableStateOf(values[0]) }
    Column {
        Text(stringResource(R.string.dark_mode), style = MaterialTheme.typography.titleMedium)
        entries.forEachIndexed { index, title ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selected == values[index], onClick = {
                    selected = values[index]
                    viewModel.saveTheme(values[index])
                })
                Text(title)
            }
        }
    }
}

@Composable
private fun DefaultTabPage(viewModel: OnboardingViewModel) {
    val entries = stringArrayResource(R.array.preference_default_tab_entries)
    val values = stringArrayResource(R.array.preference_default_tab_values)
    var selected by remember { mutableStateOf(values[0]) }
    Column {
        Text(stringResource(R.string.default_tab), style = MaterialTheme.typography.titleMedium)
        entries.forEachIndexed { index, title ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selected == values[index], onClick = {
                    selected = values[index]
                    viewModel.saveDefaultTab(values[index])
                })
                Text(title)
            }
        }
    }
}

@Composable
private fun BottomBarPage(viewModel: OnboardingViewModel, onFinish: () -> Unit) {
    val entries = stringArrayResource(R.array.preference_bottom_navigation_bar_labels_entries)
    val values = stringArrayResource(R.array.preference_bottom_navigation_bar_labels_values)
    var selected by remember { mutableStateOf(values[0]) }
    Column {
        Text(stringResource(R.string.bottom_navigation_bar_labels), style = MaterialTheme.typography.titleMedium)
        entries.forEachIndexed { index, title ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selected == values[index], onClick = {
                    selected = values[index]
                    viewModel.saveBottomBarLabels(values[index])
                })
                Text(title)
            }
        }
        Button(onClick = onFinish, modifier = Modifier.padding(top = 24.dp)) {
            Text(stringResource(R.string.finish))
        }
    }
}
